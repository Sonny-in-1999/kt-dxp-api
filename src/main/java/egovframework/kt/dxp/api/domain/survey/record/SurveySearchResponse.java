package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SurveySearchResponse(
        @ApiModelProperty(name = "설문 번호", example = "1")
        Integer surveySequenceNumber,
        @Schema(description = "설문 구분(참여가능[02], 참여완료[03], 참여불가능[05]) / varchar(36)", example = "")
        String userSurveyDivision,
        @ApiModelProperty(name = "제목", example = "설문 입니다.")
        String title,
        @ApiModelProperty(name = "시작일시", example = "2024-10-12")
        String startDate,
        @ApiModelProperty(name = "종료일시", example = "2024-11-02")
        String endDate,
        @ApiModelProperty(name = "남은 시간", example = "20")
        String limitTime,
        @ApiModelProperty(name = "참여 시작 연령", example = "20")
        Integer participationStartAge,
        @ApiModelProperty(name = "참여 종료 연령", example = "40")
        Integer participationEndAge,
        @ApiModelProperty(name = "참여가능 연령 설명/ String", example = "모든 연령/10세~20세")
        String ageDescription,
        @ApiModelProperty(name = "남성 가능 여부", example = "N")
        String maleAbleYn,
        @ApiModelProperty(name = "여성 가능 여부", example = "N")
        String femaleAbleYn,
        @ApiModelProperty(name = "참여중 인원", example = "31")
        Integer userParticipationCount
) {

}
