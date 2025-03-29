package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public record SurveySearchRequest(
        @ApiModelProperty(name = "설문 탭 구분 코드", example = "01")
        @NotEmpty
        String surveyTabDivisionCode,
        @ApiModelProperty(name = "설문 서브 구분 코드", example = "01")
        @NotEmpty
        String surveySubDivisionCode,
        @ApiModelProperty(name = "페이지 번호", example = "0")
        Integer pageNo,
        @ApiModelProperty(name = "페이지 크기", example = "10")
        Integer pageSize
) {

}
