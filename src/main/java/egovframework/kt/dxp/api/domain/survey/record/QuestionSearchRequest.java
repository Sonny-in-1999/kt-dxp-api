package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;

public record QuestionSearchRequest(
        @ApiModelProperty(name = "설문 순번", example = "1")
        @NotNull
        Integer surveySequenceNumber,
        @ApiModelProperty(name = "질문 순번", example = "1")
        @NotNull
        Integer questionSequenceNumber,
        @Schema(name = "페이지 번호", example = "0")
        Integer pageNo,
        @Schema(name = "페이지 크기", example = "10")
        Integer pageSize
) {

}
