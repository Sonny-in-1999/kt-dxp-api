package egovframework.kt.dxp.api.domain.file.record;

import io.swagger.annotations.ApiModelProperty;

public record FileResponse(
        @ApiModelProperty(name = "파일 url", example = "/v1/file/saveFileName")
        String url
) {

}
