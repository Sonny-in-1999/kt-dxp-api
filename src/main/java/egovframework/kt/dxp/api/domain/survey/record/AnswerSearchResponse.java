package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;

public record AnswerSearchResponse(
        @ApiModelProperty(name = "항목 순번", example = "1")
        Integer itemSequenceNumber,
        @ApiModelProperty(name = "답변", example = "답변 입니다.")
        String answer
) {

}
