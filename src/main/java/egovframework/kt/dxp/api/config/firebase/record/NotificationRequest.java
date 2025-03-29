package egovframework.kt.dxp.api.config.firebase.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-09-12
 */
@ApiModel("Firebase Message 전송 시 body 메시지 구조")
@Builder
public record NotificationRequest(
        @ApiModelProperty(value = "메시지 구분", notes = "Front 에서 구분에 따른 로직 분기 시 사용")
        String messageDivision,
        @ApiModelProperty(value = "메시지 제목", required = true)
        String title,
        @ApiModelProperty(value = "메시지 내용", required = true)
        String message,
        @ApiModelProperty(value = "메시지 링크", notes = "알림 클릭 시 이동 link URL")
        String link
) {
}
