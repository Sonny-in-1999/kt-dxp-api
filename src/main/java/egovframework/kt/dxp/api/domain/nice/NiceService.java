package egovframework.kt.dxp.api.domain.nice;

import egovframework.kt.dxp.api.common.CommonVariables;
import egovframework.kt.dxp.api.common.MaskingUtils;
import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.LoginCode;
import egovframework.kt.dxp.api.common.code.NiceCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.config.jwt.TokenProvider;
import egovframework.kt.dxp.api.config.jwt.record.TokenResponse;
import egovframework.kt.dxp.api.domain.nice.record.LoginCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceCheckRequest;
import egovframework.kt.dxp.api.domain.nice.record.NiceCheckResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeDataDTO;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeRequest;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeResponse;
import egovframework.kt.dxp.api.domain.nice.record.NiceDecodeResponse.NiceDecodeResponseData;
import egovframework.kt.dxp.api.domain.nice.record.UserLoginResponse;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.domain.user.enumeration.Gender;
import egovframework.kt.dxp.api.entity.M_USR;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("applicationContextHolder")
public class NiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NiceService.class);

    private final MessageConfig messageConfig;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${nice.site-code}")
    private String siteCode;
    @Value("${nice.site-pw}")
    private String sitePw;
    @Value("${nice.req-num}")
    private String reqNum;

    public NiceCheckResponse getNiceEncodedData(NiceCheckRequest parameter) {
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();
        String plainStringData = "7:REQ_SEQ" + this.reqNum.getBytes().length + ":" + this.reqNum +
                "8:SITECODE" + this.siteCode.getBytes().length + ":" + this.siteCode +
                "9:AUTH_TYPE" + parameter.authType().getBytes().length + ":" + parameter.authType() +
                "7:RTN_URL" + parameter.successURL().getBytes().length + ":" + parameter.successURL() +
                "7:ERR_URL" + parameter.errorURL().getBytes().length + ":" + parameter.errorURL() +
                "9:CUSTOMIZE" + parameter.customize().getBytes().length + ":" + parameter.customize();
        NiceCode niceResponseCode;
        String cipherData = null;
        int returnCode = niceCheck.fnEncode(this.siteCode, this.sitePw, plainStringData);
        if (returnCode == 0) {
            niceResponseCode = NiceCode.NICE_ENC_SUCCESS;
            cipherData = niceCheck.getCipherData();
        } else if (returnCode == -1) {
            niceResponseCode = NiceCode.NICE_ENC_SYSTEM_ERROR;
        } else if (returnCode == -2) {
            niceResponseCode = NiceCode.NICE_ENC_PROCESS_ERROR;
        } else if (returnCode == -3) {
            niceResponseCode = NiceCode.NICE_ENC_DATA_ERROR;
        } else if (returnCode == -9) {
            niceResponseCode = NiceCode.NICE_INPUT_DATA_ERROR;
        } else {
            niceResponseCode = NiceCode.NICE_UNKNOWN_ERROR;
        }
        String message = this.messageConfig.getMessage(niceResponseCode);
        log.info("Nice Response Message : " + message);
        return NiceCheckResponse.builder()
                                .code(niceResponseCode.code())
                                .message(message)
                                .cipherData(cipherData)
                                .build();
    }

    @SuppressWarnings("unchecked")
    public NiceDecodeResponse getNiceDecodedData(NiceDecodeRequest parameter) {
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();
        String decodedData = URLDecoder.decode(requestReplace(parameter.encodedData()), StandardCharsets.UTF_8);
        int returnCode = niceCheck.fnDecode(this.siteCode, this.sitePw, decodedData);
        NiceCode niceResponseCode;
        HashMap<String, String> parsedMap = null;
        if (returnCode == 0) {
            niceResponseCode = NiceCode.NICE_DEC_SUCCESS;
            String decodedStringData = niceCheck.getPlainData();
            parsedMap = (HashMap<String, String>) niceCheck.fnParse(decodedStringData);
            parsedMap.put("IS_ADULT", String.valueOf(Boolean.FALSE.equals(CommonUtils.isUnderAge(parsedMap.get("BIRTHDATE"), 18))));
        } else if (returnCode == -1) {
            niceResponseCode = NiceCode.NICE_DEC_SYSTEM_ERROR;
        } else if (returnCode == -4) {
            niceResponseCode = NiceCode.NICE_DEC_PROCESS_ERROR;
        } else if (returnCode == -5) {
            niceResponseCode = NiceCode.NICE_DEC_HASH_ERROR;
        } else if (returnCode == -6) {
            niceResponseCode = NiceCode.NICE_DEC_DATA_ERROR;
        } else if (returnCode == -9) {
            niceResponseCode = NiceCode.NICE_INPUT_DATA_ERROR;
        } else if (returnCode == -12) {
            niceResponseCode = NiceCode.NICE_SITE_PW_ERROR;
        } else {
            niceResponseCode = NiceCode.NICE_UNKNOWN_ERROR;
        }
        log.info("Nice Response Message : " + messageConfig.getMessage(niceResponseCode));
        return NiceDecodeResponse.builder()
                                 .code(niceResponseCode.code())
                                 .message(messageConfig.getMessage(niceResponseCode))
                                 .decodedData(
                                         (parsedMap != null) ? CommonVariables.GSON.fromJson(CommonVariables.GSON.toJson(parsedMap), NiceDecodeDataDTO.class)
                                                 : null
                                 )
                                .responseData(NiceDecodeResponseData.builder()
                                                                    .isAdult((parsedMap != null) ? parsedMap.get("IS_ADULT") : null)
                                                                    .build())
                                 .build();
    }

    @Transactional
    public LoginCheckResponse loginCheck(NiceDecodeRequest parameter) {
        NiceDecodeResponse niceDecodeResponse = getNiceDecodedData(parameter);
        NiceDecodeDataDTO niceDecodedData = niceDecodeResponse.decodedData();
        //NICE decode 정보 확인
        if (ObjectUtils.isEmpty(niceDecodedData)) {
            return LoginCheckResponse.builder()
                                     .code(niceDecodeResponse.code())
                                     .message(niceDecodeResponse.message())
                                     .build();
        }
        //회원 가입여부 확인
        Optional<M_USR> optionalUser = userRepository.findByCertificationId(niceDecodedData.getCi());

        if (optionalUser.isEmpty()) {
            return LoginCheckResponse.builder()
                                     .code(LoginCode.NOT_REGISTERED.code())
                                     .message(messageConfig.getMessage(LoginCode.NOT_REGISTERED))
                                     .userLoginResponse(
                                             UserLoginResponse.builder()
                                                              .birthDate(niceDecodedData.getBirthDate())
                                                              .isChildren(CommonUtils.isUnderAge(niceDecodedData.getBirthDate(), 24))
                                                              .gender(Gender.getGender(niceDecodedData.getGender()))
                                                              .build())
                                     .build();
        }

        if (optionalUser.get().getPassword().isEmpty()) {
            log.info("Password is empty");
            String encoderCertificationId = passwordEncoder.encode(niceDecodedData.getCi());
            optionalUser.get().updatePasswordInformation(encoderCertificationId);
            userRepository.save(optionalUser.get());
        }

        //로그인 성공 정보 전송
        /*5. 사용자 권한 체크*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(optionalUser.get().getUserId(), optionalUser.get().getCertificationId());
        Authentication authentication = authenticationManagerBuilder.getObject()
                                                                    .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        /*6. JWT 토큰 생성*/
        TokenResponse tokenResponse = tokenProvider.createToken(authentication, false, optionalUser.get());
        optionalUser.get().updateUserToken(tokenResponse.token(), tokenResponse.refreshToken());

        /* 로그인 시간 추가 */
        LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        optionalUser.get().setRecentLoginDate(currentLocalDateTime);

        /* 모바일 운영 체제 정보 */
        optionalUser.get().updateLoginOSInformation(niceDecodedData.getMobilePhoneNumber(), parameter.operatingSystemType(), parameter.pushKey());
        userRepository.save(optionalUser.get());
        // TODO. Juyoung Chae [Token 생성] 응답 정보에 Access Token, Refresh Token 정보 추가
        return LoginCheckResponse.builder()
                                 .code(LoginCode.LOGIN_SUCCESS.code())
                                 .message(messageConfig.getMessage(LoginCode.LOGIN_SUCCESS))
                                 .userLoginResponse(
                                         UserLoginResponse.builder()
                                                          .userId(optionalUser.get().getUserId())
                                                          .userName(MaskingUtils.maskName(optionalUser.get().getUserName()))
                                                          .mobilePhoneNumber(MaskingUtils.maskPhoneNumber(niceDecodedData.getMobilePhoneNumber()))
                                                          .birthDate(optionalUser.get().getBirthDate())
                                                          .isChildren(CommonUtils.isUnderAge(niceDecodedData.getBirthDate(), 24))
                                                          .gender(Gender.getGender(niceDecodedData.getGender()))
                                                          .accessToken(tokenResponse.token())
                                                          .refreshToken(tokenResponse.refreshToken())
                                                          .build()
                                 )
                                 .build();
    }

    @Transactional
    public ItemResponse<Long> logout(HttpServletRequest httpServletRequest) {

        String userId = CommonUtils.getUserId();
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));
        log.info("Logout user information: {}", userId);

        mUsr.updateLogoutInformation();
        userRepository.save(mUsr);
        SecurityContextHolder.clearContext();
        return ItemResponse.<Long>builder()
                           .status(messageConfig.getCode(LoginCode.LOGOUT_SUCCESS))
                           .message(messageConfig.getMessage(LoginCode.LOGOUT_SUCCESS))
                           .item(1L)
                           .build();
    }
    private String requestReplace(String paramValue) {
        String result = paramValue;
        if (StringUtils.isNotEmpty(result)) {
            result = result.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            result = result.replaceAll("\\*", "");
            result = result.replaceAll("\\?", "");
            result = result.replaceAll("\\[", "");
            result = result.replaceAll("\\{", "");
            result = result.replaceAll("\\(", "");
            result = result.replaceAll("\\)", "");
            result = result.replaceAll("\\^", "");
            result = result.replaceAll("\\$", "");
            result = result.replaceAll("'", "");
            result = result.replaceAll("@", "");
            result = result.replaceAll(";", "");
            result = result.replaceAll(":", "");
            result = result.replaceAll("-", "");
            result = result.replaceAll("#", "");
            result = result.replaceAll("--", "");
            result = result.replaceAll("-", "");
            result = result.replaceAll(",", "");
        }
        return result;
    }
}
