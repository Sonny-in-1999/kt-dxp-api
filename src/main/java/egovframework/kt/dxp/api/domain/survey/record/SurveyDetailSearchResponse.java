package egovframework.kt.dxp.api.domain.survey.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record SurveyDetailSearchResponse(
        @ApiModelProperty(name = "설문 번호", example = "1")
        Integer surveySequenceNumber,
        @Schema(description = "설문 구분(참여가능[02], 참여완료[03], 참여불가능[05]) / varchar(36)", example = "")
        String userSurveyDivision,
        @ApiModelProperty(name = "제목", example = "설문 입니다.")
        String title,
        @Schema(description = "내용 / text", example = "")
        String contents,
        @ApiModelProperty(name = "시작일시", example = "2024-10-12")
        String startDate,
        @ApiModelProperty(name = "종료일시", example = "2024-11-02")
        String endDate,
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
        @ApiModelProperty(name = "남은 시간 / varchar(36)", example = "3일")
        String limitTime,
        @Schema(description = "참여중 인원 / varchar(36)", example = "30")
        Integer userParticipationCount,
        @ApiModelProperty(name = "문항수", example = "4")
        Integer itemCount,
        @ApiModelProperty(name = "첨부파일 리스트", example = "")
        List<FileSearchResponse> fileList,
        @ApiModelProperty(name = "질문 리스트")
        List<QuestionItemSearchResponse> questionList
) {

        public SurveyDetailSearchResponse setQuestionList(List<QuestionItemSearchResponse> questionList) {
                return new SurveyDetailSearchResponse(this.surveySequenceNumber
                        ,this.userSurveyDivision
                        ,this.title
                        ,this.contents
                        ,this.startDate
                        ,this.endDate
                        ,this.participationStartAge
                        ,this.participationEndAge
                        ,this.ageDescription
                        ,this.maleAbleYn
                        ,this.femaleAbleYn
                        ,this.limitTime
                        ,this.userParticipationCount
                        ,this.itemCount
                        ,this.fileList
                        ,questionList);
        }

        public SurveyDetailSearchResponse setUserDivisionCode(String userSurveyDivision){
                return new SurveyDetailSearchResponse(this.surveySequenceNumber
                        ,userSurveyDivision
                        ,this.title
                        ,this.contents
                        ,this.startDate
                        ,this.endDate
                        ,this.participationStartAge
                        ,this.participationEndAge
                        ,this.ageDescription
                        ,this.maleAbleYn
                        ,this.femaleAbleYn
                        ,this.limitTime
                        ,this.userParticipationCount
                        ,this.itemCount
                        ,this.fileList
                        ,this.questionList);
        }

        public SurveyDetailSearchResponse setUserParticipationCount(Integer userParticipationCount){
                return new SurveyDetailSearchResponse(this.surveySequenceNumber
                        ,this.userSurveyDivision
                        ,this.title
                        ,this.contents
                        ,this.startDate
                        ,this.endDate
                        ,this.participationStartAge
                        ,this.participationEndAge
                        ,this.ageDescription
                        ,this.maleAbleYn
                        ,this.femaleAbleYn
                        ,this.limitTime
                        ,userParticipationCount
                        ,this.itemCount
                        ,this.fileList
                        ,this.questionList);
        }

}
