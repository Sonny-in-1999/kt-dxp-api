package egovframework.kt.dxp.api.config.firebase.record;

import com.google.firebase.ErrorCode;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-09-12
 */
@ApiModel("Firebase Message 전송 결과 응답")
@Builder
public record NotificationResultResponse(
        @ApiModelProperty(value = "사용자 ID")
        String userId,
        @ApiModelProperty(value = "사용자 메시지 토큰")
        String pushToken,
        @ApiModelProperty(value = "전송여부")
        TransmissionDivision sendYn,
        @ApiModelProperty(value = "예외 코드")
        ErrorCode exceptionCode,
        @ApiModelProperty(value = "예외 메시지")
        String exceptionMessage
) {
}
