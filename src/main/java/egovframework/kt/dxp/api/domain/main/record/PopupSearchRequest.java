package egovframework.kt.dxp.api.domain.main.record;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public record PopupSearchRequest(
        @ApiModelProperty(name = "팝업 번호 리스트", example = "")
        @NotNull
        List<Integer> popupSequenceNumber
) {
}
