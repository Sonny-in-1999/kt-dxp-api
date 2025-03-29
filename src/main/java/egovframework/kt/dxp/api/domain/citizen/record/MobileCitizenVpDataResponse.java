package egovframework.kt.dxp.api.domain.citizen.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record MobileCitizenVpDataResponse(
        @ApiModelProperty(name = "결과", example = "true, false")
        Boolean result,
        @ApiModelProperty(name = "춘천시민 여부", example = "true, false")
        UseYn isChuncheonAddress

) {

}
