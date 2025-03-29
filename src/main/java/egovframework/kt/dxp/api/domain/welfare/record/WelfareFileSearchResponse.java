package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "복지정책 파일 응답")
public record WelfareFileSearchResponse(
        @ApiModelProperty(name = "게시판 구분", example = "01")
        String bulletinBoardDivision,
        @ApiModelProperty(name = "저장 파일명", example = "파일명")
        String saveFileName,
        @ApiModelProperty(name = "실제 파일명", example = "실제 파일명")
        String actualFileName
        ) {
}