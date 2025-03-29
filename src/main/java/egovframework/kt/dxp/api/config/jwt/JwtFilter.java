package egovframework.kt.dxp.api.config.jwt;


import egovframework.kt.dxp.api.common.CommonVariables;
import egovframework.kt.dxp.api.common.contextHolder.ApplicationContextHolder;
import egovframework.kt.dxp.api.config.jwt.record.JwtValidation;
import egovframework.kt.dxp.api.domain.authority.AuthTokenService;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.M_USR;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * JWT Filter
 * Cookie 에서 Access Token 을 추출해 유효성 검증 및 중복 로그인 여부를 체크
 *
 * @author GEONLEE
 * @since 2022-11-11<br />
 * 2023-07-21 BITNA 권한별 menuUrl 접근 처리(일단 주석)<br />
 * 2023-10-17 GEONLEE - swagger 3.x update 로 requestURI.startsWith("-docs") 추가<br />
 * 2023-11-20 GEONLEE - 중복 로그인 처리 프로세스 변경. header 는 건들지 않고 response status 코드로 처리. 불필요 코드 제거<br />
 * 2023-12-04 GEONLEE - 불필요 코드 제거 및 로직 정리, 주석 추가<br />
 * 2024-03-15 GEONLEE - Access Token 이 전달되지 않았을 경우 바로 에러 응답 리턴하도록 변경, Security filter 타지 않음<br />
 * - Access Token 의 형태가 올바르지 않을 경우 바로 에러 응답 리턴하도록 변경, Security filter 타지 않음<br />
 * 2024-03-28 GEONLEE - Filter 에서 바로 응답하지 않고 JwtAuthenticationEntryPoint 로 전달 하도록 로직 개선<br />
 * 2024-04-04 GEONLEE - 버그 수정, JwtValidation 에서 token 추출, refresh token 유효성 검증 부분
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    private final List<String> ignoreUris = List.of(CommonVariables.IGNORE_URIS);
    private final TokenProvider tokenProvider;
    private final AuthTokenService authTokenService;
    private boolean hasRefreshToken = false;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestURI = httpServletRequest.getRequestURI()
                                              .replace(CommonVariables.CONTEXT_PATH, "");

        if (!ignoreUris.contains(requestURI) && !requestURI.startsWith("/v1/nice/verification")
                && !requestURI.startsWith("/swagger-") && !requestURI.startsWith("/api-docs")
                && !requestURI.startsWith("/v2/api-docs")) {
            LOGGER.info("Request URI : '{}', Start to check access token. ▼", requestURI);
            String accessToken = null;

            /*1. Cookie 에서 Access Token 추출 (NS_AUT)*/
            //accessToken = tokenProvider.getTokenFromCookie(httpServletRequest);

            //TODO: Header 에서 추출하는걸로 변경
            /*1. Header 에서 Access Token 추출 (NS_AUT)*/
            accessToken = tokenProvider.getTokenFromHeader(httpServletRequest);
            JwtValidation valid = new JwtValidation(false, null, accessToken);

            /*2. Access Token 유효성 체크*/
            if (StringUtils.hasText(accessToken)) {
                checkTokenValidity(valid, httpServletRequest, httpServletResponse);
            }
            /*3. 중복 로그인인지 체크*/
            //TODO: 중복 로그인 체크 프로세스
            // 1. 최초 회원가입하면 A 토큰 정보 발행
            // 2. 신규 휴대폰으로 로그인 B 토큰 정보 발행
            // 3. 기존 휴대폰으로 로그인시 Duplication 발생
            // 4. 2번 상황에서 중복 로그인 체크 불가능 - 로그인 할 때 토큰 정보를 비교하는 로직을 추가해야하나?
            if (valid.isValid()) {
                checkDuplicationLogin(valid, httpServletResponse, hasRefreshToken);
            }

            /*4. Spring security 에 권한 정보 저장
             * 권한 정보가 없을 경우 JwtAuthenticationEntryPoint 로 전달.(security config 에 설정)*/
            if (valid.isValid()) {
                LOGGER.info("User's token validation success: '{}'", valid.getUserId());
                Authentication authentication = tokenProvider.getAuthentication(
                        valid.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * AccessToken 유효성 체크
     *
     * @author GEONLEE
     * @since 2024-03-28
     */
    private void checkTokenValidity(JwtValidation valid,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (!StringUtils.hasText(valid.getAccessToken()) || !tokenProvider.validateToken(
                valid.getAccessToken())) {
            valid.setValid(false);
            String refreshToken = null;
            /*Access Token 이 만료 되었을 경우 RefreshToken 을 확인*/
            try {
                refreshToken = tokenProvider.getRefreshTokenFromHeader(httpServletRequest);
                if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
                    String userId = tokenProvider.getUid(refreshToken);
                    valid.setUserId(userId);
                    valid.setAccessToken(refreshToken);
                    setHasRefreshToken(true);
                    if (httpServletRequest.getRequestURI()
                                          .replace(CommonVariables.CONTEXT_PATH, "")
                                          .startsWith("/v1/valid/authenticate/reissue")) {
                        valid.setValid(true);
                    }


                    //// Refresh Token으로 중복 로그인 체크가 필요한가?
                    //ItemResponse<AuthTokenResponse> authTokenResponse = authTokenService.reIssueToken(
                    //        httpServletRequest);
                    //if (ErrorCode.DUPLICATION_LOGIN.code().equals(authTokenResponse.status())) {
                    //    httpServletResponse.setHeader("isDuplicationLogin", "true");
                    //} else {
                    //    valid.setAccessToken(authTokenResponse.item().accessToken());
                    //    valid.setValid(true);
                    //}
                }
            } catch (NullPointerException e) {
                LOGGER.error("Refresh token extraction failed.");
            }
        } else {
            String userId = tokenProvider.getUid(valid.getAccessToken());
            valid.setValid(true);
            valid.setUserId(userId);
            setHasRefreshToken(false);
        }
    }

    /**
     * 중복 로그인 여부 체크<br />
     * DB의 Access Token 과 비교<br />
     *
     * @author GEONLEE
     * @since 2024-03-28
     */
    private void checkDuplicationLogin(JwtValidation valid, HttpServletResponse httpServletResponse,
            Boolean hasRefreshToken) {
        UserRepository userRepository = ApplicationContextHolder.getContext()
                                                                .getBean(UserRepository.class);
        Optional<M_USR> optionalEntity;
        if (Boolean.TRUE.equals(hasRefreshToken)) {
            optionalEntity = userRepository.findOneByUserIdAndRefreshToken(valid.getUserId(), valid.getAccessToken());
            if (optionalEntity.isEmpty()) {
                LOGGER.error("User's refresh token is different. (Duplicated login): '{}'", valid.getUserId());
                valid.setValid(false);
                httpServletResponse.setHeader("isDuplicationLogin", "true");
            }
        } else {
            optionalEntity = userRepository.findOneByUserIdAndAccessToken(valid.getUserId(), valid.getAccessToken());
            if (optionalEntity.isEmpty()) {
                LOGGER.error("User's access token is different. (Duplicated login): '{}'", valid.getUserId());
                valid.setValid(false);
                httpServletResponse.setHeader("isDuplicationLogin", "true");
            }
        }
    }

    private void setHasRefreshToken (boolean hasRefreshToken) {
        this.hasRefreshToken = hasRefreshToken;
    }
}