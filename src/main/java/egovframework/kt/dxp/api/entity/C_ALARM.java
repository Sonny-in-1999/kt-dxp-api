package egovframework.kt.dxp.api.entity;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class C_ALARM {

    /* 사용자 아이디             */
    @Id
    @Size(max = 36)
    @Column(name = "USR_ID", nullable = false, length = 36)
    private String userId;

    ///* 푸시 알람 여부            */
    //@Size(max = 1)
    //@NotNull
    //@Column(name = "PUSH_ALARM_YN", nullable = false, length = 1)
    //private String pushAlarmYn;

    ///* 야간 푸시 알람 여부       */
    //@Size(max = 1)
    //@NotNull
    //@Column(name = "NT_PUSH_ALARM_YN", nullable = false, length = 1)
    //private String nightPushAlarmYn;

    /* 활동 알람 여부            */
    @NotNull
    @Column(name = "ACT_ALARM_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn actAlarmYn;

    /* 공지 알람 여부            */
    @NotNull
    @Column(name = "NOTICE_ALARM_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn noticeAlarmYn;

    /* 혜택(보상) 알람 여부          */
    @NotNull
    @Column(name = "RWD_ALARM_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn rewardAlarmYn;

    /* 수정 일시                 */
    @NotNull
    @Column(name = "UPDT_DT", nullable = false)
    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name = "USR_ID")
    private M_USR mUsr;

    @Builder
    public C_ALARM(String userId,
            UseYn actAlarmYn, UseYn noticeAlarmYn,
            UseYn rewardAlarmYn, LocalDateTime updateDate,
            M_USR mUsr) {
        this.userId = userId;
        this.actAlarmYn = actAlarmYn;
        this.noticeAlarmYn = noticeAlarmYn;
        this.rewardAlarmYn = rewardAlarmYn;
        this.updateDate = updateDate;
        this.mUsr = mUsr;
    }

    /* 현황 알람 계정 정보 삭제 */
    public void deleteAlarmInformation() {
        //this.nightPushAlarmYn = "N";
        this.actAlarmYn = UseYn.N;
        this.noticeAlarmYn = UseYn.N;
        this.rewardAlarmYn = UseYn.N;
        this.updateDate = LocalDateTime.now();
    }
}