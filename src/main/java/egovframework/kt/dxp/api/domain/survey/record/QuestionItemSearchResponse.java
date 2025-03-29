package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

public record QuestionItemSearchResponse(
        @ApiModelProperty(name = "질문 순번", example = "1")
        Integer questionSequenceNumber,
        @ApiModelProperty(name = "질문 내용", example = "질문 입니다.")
        String question,
        @ApiModelProperty(name = "질문 유형", example = "01")
        String questionType,
        @ApiModelProperty(name = "질문 유형값", example = "객관식")
        String questionTypeValue,
        @ApiModelProperty(name = "답변", example = "답변 입니다.")
        List<AnswerSearchResponse> answer,
        @ApiModelProperty(name = "필수여부", example = "Y")
        String essentialYn,
        @Schema(description = "최소 선택 제한수", example = "")
        Integer minSelectCount,
        @Schema(description = "최대 선택 제한수", example = "")
        Integer maxSelectCount,
        @ApiModelProperty(name = "항목 리스트")
        List<QuestionSearchResponse> itemList
) {

    @Builder
    public QuestionItemSearchResponse(
            Integer questionSequenceNumber,
            String question,
            String questionType,
            String questionTypeValue,
            List<AnswerSearchResponse> answer,
            String essentialYn,
            Integer minSelectCount,
            Integer maxSelectCount,
            List<QuestionSearchResponse> itemList) {
        this.questionSequenceNumber = questionSequenceNumber;
        this.question = question;
        this.questionType = questionType;
        this.questionTypeValue = questionTypeValue;
        this.answer = answer;
        this.essentialYn = essentialYn;
        this.minSelectCount = minSelectCount;
        this.maxSelectCount = maxSelectCount;
        this.itemList = itemList;
    }

    public QuestionItemSearchResponse setItemList(List<QuestionSearchResponse> itemList) {
        return new QuestionItemSearchResponse(this.questionSequenceNumber,
                this.question, this.questionType, this.questionTypeValue, this.answer,
                this.essentialYn, this.minSelectCount, this.maxSelectCount, itemList);
    }
}
