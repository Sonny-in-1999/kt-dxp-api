package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;

public record MobileCitizenVPRequest(
        @ApiModelProperty(name = "발급 신분증 코드", example = "M01:춘천 디지털 신분증, P01:임산부증, L01:춘천 도서관 출입증, C01:문화센터 출입증")
        String identityCode,

        @ApiModelProperty(name = "결과", example = "true, false")
        /** 결과 */
        Boolean result,
        /** 데이터 */
        @ApiModelProperty(name = "데이터", example = "eyJjbWQiOiI1MzAiLCJtb2RlIjoiaW5kaXJlY3QiLCJzdmNDb2RlIj")
        String data
) {

}
