package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;


public record FileDownloadRequest(
        @ApiModelProperty(name = "파일 순번", example = "1")
        @NotEmpty
        Integer fileSequenceNumber
) {
}
