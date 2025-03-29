package egovframework.kt.dxp.api.domain.authority.record;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Builder;

@Builder
public record AuthTokenResponse(
        @ApiModelProperty(value = "New Access token")
        String accessToken
)implements
        Serializable {
}