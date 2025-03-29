package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;

public record MobileCitizenDetailRequest(
        @ApiModelProperty(name = "거래코드", example = "2024122611404765991A79C31")
        String trxCode
) {

}
