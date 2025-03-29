package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public record MobileCitizenBookmarkRequest(
        @ApiModelProperty(name = "TRX 코드", example = "20250110053301055A662DFBE")
        @NotEmpty
        String trxCode,
        @ApiModelProperty(name = "즐겨찾기 여부", example = "Y(즐겨찾기 추가)/N(즐겨찾기 해제)")
        @NotEmpty
        String bookmarkYn
) {
}
