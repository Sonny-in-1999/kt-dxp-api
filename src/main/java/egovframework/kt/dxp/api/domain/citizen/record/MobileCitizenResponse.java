package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

//검증 API 데이터
@Builder
public record MobileCitizenResponse(
        @ApiModelProperty(name = "결과", example = "true, false")
        Boolean result,
        @ApiModelProperty(name = "데이터", example = "eyJjbWQiOiI1MzAiLCJtb2RlIjoiaW5kaXJlY3QiLCJzdmNDb2RlIj...")
        String data
) {

}
