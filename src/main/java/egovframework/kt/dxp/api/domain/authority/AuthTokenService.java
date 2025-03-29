package egovframework.kt.dxp.api.domain.authority;

import egovframework.kt.dxp.api.common.MaskingUtils;
import egovframework.kt.dxp.api.common.code.AuthTokenCode;
import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.config.jwt.TokenProvider;
import egovframework.kt.dxp.api.domain.authority.record.AuthTokenResponse;
import egovframework.kt.dxp.api.domain.authority.record.AuthValidResponse;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.M_USR;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class AuthTokenService {
    private final MessageConfig messageConfig;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ItemResponse<AuthValidResponse> validAuthorize(HttpServletRequest httpServletRequest) {

        String userId = CommonUtils.getUserId();

        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));

        // 서비스 생성 후 [userId, userName, birthDate, isChildren, mobilePhoneNumber- 포함]해서 리턴
        AuthValidResponse authValidResponse = AuthValidResponse.builder()
                .userId(mUsr.getUserId())
                .userName(MaskingUtils.maskName(mUsr.getUserName()))
                .birthDate(mUsr.getBirthDate())
                .isChildren(CommonUtils.isUnderAge(mUsr.getBirthDate(), 24))
                .gender(mUsr.getGenderType())
                .mobilePhoneNumber(MaskingUtils.maskPhoneNumber(mUsr.getMobilePhoneNumber()))
                .build();

        return ItemResponse.<AuthValidResponse>builder()
                           .status(messageConfig.getCode(AuthTokenCode.AUTHENTICATION_SUCCESS))
                           .message(messageConfig.getMessage(AuthTokenCode.AUTHENTICATION_SUCCESS))
                           .item(authValidResponse)
                           .build();
    }

    @Transactional
    public ItemResponse<AuthTokenResponse> reIssueToken(HttpServletRequest httpServletRequest) {

        String statusCode     = null;
        String messageCode    = null;
        String newAccessToken = null;
        String refreshToken   = tokenProvider.getRefreshTokenFromHeader(httpServletRequest);

        if(!StringUtils.hasText(refreshToken)) {
            //TODO: 재 발급 요청이지만 헤더에 refreshToken이 없는 경우 무슨 에러?
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        if (StringUtils.hasText(refreshToken) && tokenProvider.isTokenExpired(refreshToken)) {
            //TODO: 재 발급 요청이지만 refreshToken이 만료인 경우
            throw new ServiceException(ErrorCode.EXPIRED_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userId = tokenProvider.getUid(refreshToken);
        newAccessToken = tokenProvider.createAccessToken(authentication);

        /*Refresh Token 으로 User 를 조회 해  Access Token 갱신*/
        Optional<M_USR> optionalUser = userRepository.findOneByUserIdAndRefreshToken(userId, refreshToken);
        if (optionalUser.isPresent()) {

            LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

            optionalUser.get().setAccessToken(newAccessToken);
            optionalUser.get().setRecentLoginDate(currentLocalDateTime);
            userRepository.save(optionalUser.get());

            statusCode  = AuthTokenCode.REISSUE_TOKEN_SUCCESS.code();
            messageCode = AuthTokenCode.REISSUE_TOKEN_SUCCESS.messageCode();
            log.info("Renew user's access token with refresh token: '{}'", userId);
        } else {
            log.error("User's refresh token is different. (Duplicated login): '{}'", userId);
            newAccessToken = null;
            statusCode  = ErrorCode.DUPLICATION_LOGIN.code();
            messageCode = ErrorCode.DUPLICATION_LOGIN.messageCode();
        }

        AuthTokenResponse authTokenResponse = AuthTokenResponse.builder()
                                                               .accessToken(newAccessToken)
                                                               .build();
        return ItemResponse.<AuthTokenResponse>builder()
                           .status(statusCode)
                           .message(messageCode)
                           .item(authTokenResponse)
                           .build();
    }
}
