package egovframework.kt.dxp.api.domain.notification.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 알림 설정 조회 응답
 *
 * @author GEONLEE
 * @since 2024-10-21<br />
 */
@Schema(description = "알림 설정 조회 응답")
public record NotificationSearchResponse(
        @Schema(description = "사용자 아이디 / varchar(36)", example = "")
        String userId,
        //@Schema(description = "야간 푸시 알람 여부 / varchar(1)", example = "Y")
        //String nightPushAlarmYn,
        @Schema(description = "활동 알람 여부 / varchar(1)", example = "Y")
        String actAlarmYn,
        @Schema(description = "공지 알람 여부 / varchar(1)", example = "Y")
        String noticeAlarmYn,
        @Schema(description = "혜택(보상) 알람 여부 / varchar(1)", example = "Y")
        String rewardAlarmYn,
        @Schema(description = "수정 일시 / datetime", example = "")
        String updateDate) {

}
