package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
@ApiModel(description = "신분증 상태 확인/관리 응답")
public record MobileCitizenStatusResponse(
        @ApiModelProperty(value = "발급기관 코드", example = "M01")
        String issuerCode,

        @ApiModelProperty(value = "거래 코드", example = "202412230441328264E1FED41")
        String trxCode,

        @ApiModelProperty(value = "발급 상태 코드", example = "REQUESTED")
        String issuedStatusCode,

        @ApiModelProperty(value = "생성일시")
        String createDate
) {}