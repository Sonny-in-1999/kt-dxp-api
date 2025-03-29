package egovframework.kt.dxp.api.domain.manageNotice.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@Builder
public record FileUploadResponse(
        @ApiModelProperty(name = "파일 순번", example = "1")
        Integer noticeSequenceNumber,
        @ApiModelProperty(name = "파일 명", example = "파일명")
        String actualFileName
) {

}
