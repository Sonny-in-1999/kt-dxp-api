package egovframework.kt.dxp.api.domain.main.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record BannerSearchResponse(
        @ApiModelProperty(name = "베너 순번", example = "1")
        Integer bannerSequenceNumber,
        @ApiModelProperty(name = "제목", example = "베너1")
        String title,
        @ApiModelProperty(name = "링크 URL", example = "베너 URL")
        String linkUniformResourceLocator,
        @ApiModelProperty(name = "첨부파일 리스트", example = "")
        List<FileSearchResponse> fileList
) {

}
