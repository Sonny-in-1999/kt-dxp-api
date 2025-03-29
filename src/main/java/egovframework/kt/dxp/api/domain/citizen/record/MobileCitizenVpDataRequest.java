package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record MobileCitizenVpDataRequest(
        @ApiModelProperty(name = "결과", example = "true, false")
        Boolean result,
        /** 데이터 */
        @ApiModelProperty(name = "데이터", example = "eyJjbWQiOiI1MzAiLCJtb2RlIjoiaW5kaXJlY3QiLCJzdmNDb2RlIj")
        String data
) {

}
