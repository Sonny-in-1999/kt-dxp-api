package egovframework.kt.dxp.api.config.firebase.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author GEONLEE
 * @since 2024-09-12
 */
@ApiModel("Firebase Message 전송 요청")
public record MessageRequest(
        @ApiModelProperty(value = "사용자 ID", required = true)
        String userId,
        @ApiModelProperty(value = "메시지 구분", notes = "Front 에서 구분에 따른 로직 분기 시 사용")
        String messageType,
        @ApiModelProperty(value = "메시지 제목", required = true)
        String title,
        @ApiModelProperty(value = "메시지 내용", required = true)
        String message,
        @ApiModelProperty(value = "메시지 링크", notes = "알림 클릭 시 이동 link URL")
        String link,
        @ApiModelProperty(value = "사용자 메시지 토큰", required = true)
        String pushToken
) {
}
