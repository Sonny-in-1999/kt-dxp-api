package egovframework.kt.dxp.api.domain.pushMessage.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 추가 요청")
public record PushMessageSaveRequest(

        @Schema(description = "알림 메세지 제목", example = "알림입니다.")
        String title,

        @Schema(description = "알림 메시지 본문", example = "알람 본문 입니다.")
        String message,

        @Schema(description = "링크", example = "http://XXXX.XXXX.XXXX")
        String link,

        @Schema(description = "메세지 구분", example = "A01")
        String messageType,

        @Schema(description = "그룹 코드 아이디", example = "BBS_DIV")
        String groupCodeId,

        @Schema(description = "코드 아이디", example = "01")
        String codeId
) {
}
