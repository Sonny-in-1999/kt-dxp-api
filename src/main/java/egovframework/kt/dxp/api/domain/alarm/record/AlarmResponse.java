package egovframework.kt.dxp.api.domain.alarm.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record AlarmResponse(
        @ApiModelProperty(name = "알림 유무 Y/N", example = "Y")
        UseYn hasRedBadgePushMessage,
        @ApiModelProperty(name = "공지사항 유무 Y/N", example = "N")
        UseYn hasRedBadgeNotice
)implements
        Serializable {
}