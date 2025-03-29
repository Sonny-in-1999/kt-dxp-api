package egovframework.kt.dxp.api.domain.mileage.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record MileageSearchResponse(
        @ApiModelProperty(name = "마일리지 총합", example = "700000")
        Long total_mileage,
        @ApiModelProperty(name = "마일리지 리스트", example = "")
        List<MileageDetailSearchResponse> data,
        @ApiModelProperty(name = "결과 코드", hidden = true)
        String result_code,
        @ApiModelProperty(name = "결과 메시지", hidden = true)
        String result_msg
) {

}
