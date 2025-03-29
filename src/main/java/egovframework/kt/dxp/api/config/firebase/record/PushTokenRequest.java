package egovframework.kt.dxp.api.config.firebase.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-09-12
 */
@ApiModel("Firebase Message 전송 요청 사용자")
@Builder
public record PushTokenRequest(
        @ApiModelProperty(value = "사용자 ID")
        String userId,
        @ApiModelProperty(value = "사용자 메시지 토큰")
        String pushToken
) {
}
