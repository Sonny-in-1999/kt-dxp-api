package egovframework.kt.dxp.api.domain.file.record;

import io.swagger.annotations.ApiModelProperty;

public record FileRequest(
        @ApiModelProperty(name = "게시판 구분", example = "02")
        String bulletinBoardDivision,
        @ApiModelProperty(name = "저장 파일 명", example = "455270b2-d077-4f08-854a-7e9c668a0c81")
        String saveFileName
) {

}
