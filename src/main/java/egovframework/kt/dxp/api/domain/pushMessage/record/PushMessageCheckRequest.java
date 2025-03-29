package egovframework.kt.dxp.api.domain.pushMessage.record;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   : 알림 조회 링크 요청
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Schema(description = "알림 확인 요청")
public record PushMessageCheckRequest(
        @NotEmpty
        @Schema(description = "생성 일시", example = "2024-10-30 14:40:55")
        String createDate,
        @NotEmpty
        @Schema(description = "메시지 구분", example = "A01")
        String messageType,
        @NotEmpty
        @Schema(description = "링크", example = "http://XXXX.XXXX.XXXX")
        String link
) {

}
