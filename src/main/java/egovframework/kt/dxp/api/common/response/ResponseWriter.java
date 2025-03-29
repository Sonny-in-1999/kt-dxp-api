package egovframework.kt.dxp.api.common.response;

import egovframework.kt.dxp.api.common.CommonVariables;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletResponse;
import org.springframework.http.MediaType;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : ResponseWriter
 * Description   :  
 * Creation Date : 2024-10-22
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-22, Juyoung Chae, 최초작성
 ******************************************************************************************/
public class ResponseWriter {

    /**
     * Response writer 메서드
     *
     * @author GEONLEE
     * @since 2022-11-11<br />
     * 2024-03-29 setResponseWriter tokenResponse parameter 제거<br />
     * 2024-07-10 commonUtil -> Response 패키지로 이동
     */
    public static void setResponseWriter(
            ServletResponse response, String resultCode, String resultMsg) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(CommonVariables.GSON.toJson(ErrorResponse.builder()
                                                                  .status(resultCode)
                                                                  .message(resultMsg)
                                                                  .build()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
