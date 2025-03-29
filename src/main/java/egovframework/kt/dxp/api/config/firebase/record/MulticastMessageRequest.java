package egovframework.kt.dxp.api.config.firebase.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 여러 기기에 동일한 메시지를 전송할 때 사용
 *
 * @author GEONLEE
 * @since 2024-10-29
 */
@ApiModel("Firebase Multicast Message 전송 요청")
public record MulticastMessageRequest(
        @ApiModelProperty(value = "메시지 구분", notes = "Front 에서 구분에 따른 로직 분기 시 사용")
        String messageDivision,
        @ApiModelProperty(value = "메시지 제목", required = true)
        String title,
        @ApiModelProperty(value = "메시지 내용", required = true)
        String message,
        @ApiModelProperty(value = "메시지 링크", notes = "알림 클릭 시 이동 link URL")
        String link,
        @ApiModelProperty(value = "전송할 대상 List", required = true)
        List<PushTokenRequest> pushTokenRequestList
) {
}
