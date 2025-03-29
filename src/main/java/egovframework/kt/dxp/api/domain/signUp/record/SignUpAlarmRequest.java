package egovframework.kt.dxp.api.domain.signUp.record;

import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Builder;

/**
 * DTO for {@link M_USR}
 */
@Builder
@Schema(description = "회원 가입 추가 요청")
public record SignUpAlarmRequest(

        ///* 야간 푸시 알람 여부       */
        //@Schema(description = "야간 푸시 알람 여부 / varchar(1)", example = "N")
        //@NotNull @Size(max=1)
        //String nightPushAlarmYn,

        /* 활동 알람 여부            */
        @Schema(description = "활동 알람 여부 / varchar(1)", example = "Y")
        @NotNull
        @Enumerated(EnumType.STRING)
        UseYn actAlarmYn,

        /* 공지 알람 여부            */
        @Schema(description = "공지 알람 여부 / varchar(1)", example = "Y")
        @NotNull
        @Enumerated(EnumType.STRING)
        UseYn noticeAlarmYn,

        /* 혜택(보상) 알람 여부          */
        @Schema(description = "혜택(보상) 알람 여부 / varchar(1)", example = "Y")
        @NotNull
        @Enumerated(EnumType.STRING)
        UseYn rewardAlarmYn

)implements

Serializable {

}