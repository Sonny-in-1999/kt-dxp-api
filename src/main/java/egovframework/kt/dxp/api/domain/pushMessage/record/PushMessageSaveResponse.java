package egovframework.kt.dxp.api.domain.pushMessage.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 추가 응답")
public record PushMessageSaveResponse(
        @Schema(description = "메세지 구분", example = "알람")
        String messageType,
        @Schema(description = "제목", example = "알람")
        String title,
        @Schema(description = "메세지", example = "알람 입니다.")
        String message,
        @Schema(description = "링크", example = "http://XXXX.XXXX.XXXX")
        String link,
        @Schema(description = "알람 생성 일시", example = "2024-10-17 00:00:00")
        String createDate,
        @Schema(description = "전송 요청 일시", example = "2024-10-17 00:00:00")
        String transmissionRequestDate
) {
}
