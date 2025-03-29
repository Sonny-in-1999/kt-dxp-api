package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record MobileCitizenVpBase64Request(
        /** 데이터 */
        @ApiModelProperty(name = "데이터", example = "eyJjbWQiOiI1MzAiLCJtb2RlIjoiaW5kaXJlY3QiLCJzdmNDb2RlIj")
        String vp
) {

}
