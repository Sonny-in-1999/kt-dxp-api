package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.annotations.ApiModelProperty;

public record FileSearchResponse(
        @ApiModelProperty(name = "게시판 구분", example = "01")
        String bulletinBoardDivision,
        @ApiModelProperty(name = "저장 파일명", example = "저장 파일명")
        String saveFileName,
        @ApiModelProperty(name = "실제 파일명", example = "실제 파일명")
        String actualFileName,
        @ApiModelProperty(name = "확장자", example = "확장자")
        String fileExtension,
        @ApiModelProperty(name = "파일 크기", example = "파일 크기")
        String fileSize
) {

}
