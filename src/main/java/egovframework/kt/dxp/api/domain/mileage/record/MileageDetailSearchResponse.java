package egovframework.kt.dxp.api.domain.mileage.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record MileageDetailSearchResponse(
        @ApiModelProperty(name = "마일리지 유형", example = "700000")
        String mileage_type,
        @ApiModelProperty(name = "마일리지", example = "")
        Long mileage
) {

}
