package egovframework.kt.dxp.api.domain.pushMessage.record;

import io.swagger.v3.oas.annotations.media.Schema;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   : 알림 조회 응답
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Schema(description = "알림 조회 응답")
public record PushMessageSearchResponse(
        @Schema(description = "생성 일시", example = "2024-10-30 14:40:55")
        String createDate,
        @Schema(description = "메세지 유형 코드", example = "A01")
        String messageType,
        @Schema(description = "메세지 유형", example = "공지 사항 알림")
        String messageTypeName,
        @Schema(description = "제목", example = "알람")
        String title,
        @Schema(description = "메세지", example = "알람 입니다.")
        String message,
        @Schema(description = "링크", example = "http://XXXX.XXXX.XXXX")
        String link,
        @Schema(description = "알람 확인 여부", example = "Y")
        String alarmCheckYn,
        @Schema(description = "경과 시간", example = "1시간 전")
        String elapsedTime
) {

}