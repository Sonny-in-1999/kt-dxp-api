package egovframework.kt.dxp.api.config.jwt;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 401 UNAUTHORIZED (인증 실패) 처리용 응답 클래스
 * 인증되지 않은 사용자가 인증이 필요한 요청 엔드포인트로 접근하려 할 때 발생
 *
 * @author GEONLEE
 * @since 2022-11-11<br />
 * 2023-07-21 BITNA 권한별 menuUrl 접근 처리<br />
 * 2023-11-20 GEONLEE 중복 로그인 처리 방식 변경<br />
 * 기존에 duplicationLogin 값을 header 에서 추출하는 방식에서 중복로그인 코드로 처리하도록 변경, Access token 없을 때 처리 추가<br />
 * 2023-11-22 GEONLEE RTR(Refresh Token Rotation) 갱신 1번만 가능하도록 적용<br />
 * 2023-12-04 GEONLEE setResponseWriter - Response writer 메서드 추가, 불필요 코드 제거 및 로직 정리<br />
 * 2024-03-15 GEONLEE - setResponseWriter 메서드 CommonUtils 로 이동<br />
 * 2024-03-26 GEONLEE - token 만료 시 Operator 의 권한에 접근하기 위해 @Transactional 추가<br />
 * 2024-03-28 GEONLEE - doXssFilter JwtFilter 로 이동, 기존 기능을 JwtFilter 에서 처리하고 응답만 리턴하도록 로직 정리<br />
 * 2024-03-29 GEONLEE - 중복 로그인 응답 처리 구분<br />
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageConfig messageConfig;
    private final TokenProvider tokenProvider;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) {

        ErrorCode errorCode = ErrorCode.NOT_AUTHENTICATION;

        if (StringUtils.hasText(response.getHeader("isDuplicationLogin"))) {
            errorCode = ErrorCode.DUPLICATION_LOGIN;
        } else {
            String refreshToken = tokenProvider.getRefreshTokenFromHeader(request);
            if (StringUtils.hasText(refreshToken) && tokenProvider.isTokenExpired(refreshToken)) {
                errorCode = ErrorCode.EXPIRED_TOKEN;
            } else {
                String accessToken = tokenProvider.getTokenFromHeader(request);
                if (StringUtils.hasText(accessToken) && tokenProvider.isTokenExpired(accessToken)) {
                    errorCode = ErrorCode.EXPIRED_TOKEN;
                }
            }
        }
        ResponseWriter.setResponseWriter(response, messageConfig.getCode(errorCode), messageConfig.getMessage(errorCode));
    }

    /**
     * @author GEONLEE
     * 크로스 스크립팅 방지
     */
    private void doXssFilster(String origin) {
        origin.replaceAll("'", "&#x27;").replaceAll("\"", "&quot;").replaceAll("\\(", "&#40;")
              .replaceAll("\\)", "&#41;").replaceAll("/", "&#x2F;").replaceAll("<", "&lt;")
              .replaceAll(">", "&gt;")
              .replaceAll("&", "&amp;");
    }
}
