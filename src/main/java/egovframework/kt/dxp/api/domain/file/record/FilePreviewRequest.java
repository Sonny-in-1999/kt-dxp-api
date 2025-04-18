package egovframework.kt.dxp.api.domain.file.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record FilePreviewRequest(
        @ApiModelProperty(name = "게시판 구분", example = "02")
        String bulletinBoardDivision,
        @ApiModelProperty(name = "저장 파일 명", example = "455270b2-d077-4f08-854a-7e9c668a0c81")
        String saveFileName,
        @Schema(description = "이미지 타입 요청", example = "original, thumbnail")
        String imageRequest
) {

}
