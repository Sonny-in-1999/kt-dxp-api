package egovframework.kt.dxp.api.domain.notification.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 알림 설정 수정 요청
 *
 * @author BITNA
 * @since 2024-10-21<br />
 */
@Schema(description = "알림 설정 수정 요청")
@Builder
public record NotificationModifyRequest(
        @Schema(description = "사용자 아이디 / varchar(36)", example = "", hidden = true)
        String userId,
        //@Schema(description = "야간 푸시 알람 여부 / varchar(1)", example = "")
        //String nightPushAlarmYn,
        @Schema(description = "활동 알람 여부 / varchar(1)", example = "")
        String actAlarmYn,
        @Schema(description = "공지 알람 여부 / varchar(1)", example = "")
        String noticeAlarmYn,
        @Schema(description = "혜택(보상) 알람 여부 / varchar(1)", example = "")
        String rewardAlarmYn) {

    @Builder
    public NotificationModifyRequest(
            String userId,
//            String pushAlarmYn,
//            String nightPushAlarmYn,
            String actAlarmYn, String noticeAlarmYn,
            String rewardAlarmYn) {
        this.userId = userId;
//        this.pushAlarmYn = pushAlarmYn;
//        this.nightPushAlarmYn = nightPushAlarmYn;
        this.actAlarmYn = actAlarmYn;
        this.noticeAlarmYn = noticeAlarmYn;
        this.rewardAlarmYn = rewardAlarmYn;
    }
}
