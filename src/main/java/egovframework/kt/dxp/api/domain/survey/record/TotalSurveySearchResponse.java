package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;

@Builder
public record TotalSurveySearchResponse(
        @ApiModelProperty(name = "총 건수", example = "2")
        Integer totalCount,
        @ApiModelProperty(name = "설문")
        List<SurveySearchResponse> surveyList
) {

}
