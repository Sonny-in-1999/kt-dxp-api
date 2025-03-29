package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record StartApp2AppRequest(
        @ApiModelProperty(name = "Command", example = "530")
        String cmd,
        @ApiModelProperty(name = "모드", example = "indirect,direct")
        String mode,
        @ApiModelProperty(name = "서비스코드", example = "chuncheon.1")
        String svcCode
) {
}
