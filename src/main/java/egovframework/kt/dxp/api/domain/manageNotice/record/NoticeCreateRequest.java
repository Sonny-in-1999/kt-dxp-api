package egovframework.kt.dxp.api.domain.manageNotice.record;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

public record NoticeCreateRequest(
        @ApiModelProperty(name = "공지 구분", example = "01")
        @NotEmpty
        String noticeDivision,
        @ApiModelProperty(name = "제목", example = "제목")
        @NotEmpty
        String title,
        @ApiModelProperty(name = "내용", example = "공지 입니다.")
        @NotEmpty
        String contents
) {

}
