package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

public record SurveyFileSearchResponse(
        @ApiModelProperty(name = "파일명", example = "")
        String fileName,
        @ApiModelProperty(name = "파일 확장자", example = "")
        String fileExtension,
        @ApiModelProperty(name = "base64 image", example = "")
        String image
) {
        @Builder
        public SurveyFileSearchResponse (String fileName, String fileExtension, String image) {
                this.fileName = fileName;
                this.fileExtension = fileExtension;
                this.image = image;
        }

}
