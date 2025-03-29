package egovframework.kt.dxp.api.domain.main.popup.record;

import io.swagger.annotations.ApiModelProperty;

public record PopupCheckRequest(
        @ApiModelProperty(name = "팝업 순번", example = "1")
        Integer popupSequenceNumber,
        @ApiModelProperty(name = "팝업 유형", example = "01")
        String popupType
) {
}
