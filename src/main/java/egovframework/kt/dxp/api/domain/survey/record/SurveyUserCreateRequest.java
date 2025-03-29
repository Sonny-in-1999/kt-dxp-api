package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;

public record SurveyUserCreateRequest(
        @ApiModelProperty(name = "설문 순번", example = "1")
        @NotNull
        Integer surveySequenceNumber,
        @ApiModelProperty(name = "질문 리스트")
        @NotNull
        List<QuestionCreateRequest> questionSequenceNumberList
) {

}
