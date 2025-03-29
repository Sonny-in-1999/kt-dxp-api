package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public record SurveyDetailSearchRequest(
        @ApiModelProperty(name = "설문 순번", example = "1")
        @NotNull
        Integer surveySequenceNumber
) {

}
