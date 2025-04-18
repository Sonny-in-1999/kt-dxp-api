package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;

public record MobileCitizenManualRequest(
        @ApiModelProperty(name = "동 이름", example = "봉의동")
        String dongName,
        @ApiModelProperty(name = "발급 신분증 코드", example = "M01:춘천 디지털 신분증, P01:임산부증, L01:춘천 도서관 출입증, C01:문화센터 출입증")
        String identityCode
) {

}
