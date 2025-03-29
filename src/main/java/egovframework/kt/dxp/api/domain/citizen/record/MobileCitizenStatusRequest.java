package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "신분증 상태 확인/관리 요청")
public record MobileCitizenStatusRequest(
        @ApiModelProperty(value = "발급기관 코드", required = true, example = "M01")
        String issuerCode,

        @ApiModelProperty(value = "삭제 여부", required = false, example = "false")
        boolean isDelete,

        @ApiModelProperty(value = "거래 코드", required = true, example = "202412230441328264E1FED41")
        String trxCode,

        @ApiModelProperty(value = "승인 유형 코드", required = true, example = "MANUAL", allowableValues = "MANUAL,AUTO")
        String approvalTypeCode
) {}