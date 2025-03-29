package egovframework.kt.dxp.api.config.jwt;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : JwtAccessDeniedHandler
 * Description   :  
 * Creation Date : 2024-10-22
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-22, Juyoung Chae, 최초작성
 ******************************************************************************************/

import egovframework.kt.dxp.api.common.CommonVariables;
import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ErrorResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 403 FORBIDDEN Exception 처리 응답 클래스 - 클라이언트 접근 거부<br />
 * HTTP Status 는 200으로, 응답 코드는 FORBIDDEN 으로 전송<br />
 * 인증은 완료됐으나 해당 엔드포인트에 접근할 권한이 없는 경우 동작
 * @author GEONLEE
 * @since 2022-01-11<br />
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    private TokenProvider tokenProvider;
    private final MessageConfig messageConfig;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        PrintWriter writer = response.getWriter();
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        LOGGER.info("{}, URI: {}", errorCode, request.getRequestURI());

        try {
            String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
            if (refreshToken != null) {
                String userId = tokenProvider.getUid(refreshToken);
                LOGGER.info("userId : {}, refresh Token : {}", userId, refreshToken);
            }

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(CommonVariables.GSON.toJson(ErrorResponse.builder()
                                                                  .status(messageConfig.getCode(errorCode))
                                                                  .message(messageConfig.getMessage(errorCode))
                                                                  .build()));
        } catch (NullPointerException e) {
            LOGGER.error("Create fail to message", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}