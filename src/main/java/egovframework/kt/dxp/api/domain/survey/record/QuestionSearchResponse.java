package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.math.BigDecimal;

public record QuestionSearchResponse(
        @ApiModelProperty(name = "항목 순번", example = "1")
        Integer itemSequenceNumber,
        @ApiModelProperty(name = "항목 내용", example = "항목 입니다.")
        String item,
        @ApiModelProperty(name = "항목 유형", example = "N")
        String itemType,
        @ApiModelProperty(name = "항목 유형값", example = "일반")
        String itemTypeValue,
        @ApiModelProperty(name = "항목 선택 수", example = "")
        Integer selectedCount,
        @ApiModelProperty(name = "항목 선택 비율", example = "")
        String itemSelectedRate
) {

    @Builder
    public QuestionSearchResponse(
            Integer itemSequenceNumber,
            String item,
            String itemType,
            String itemTypeValue,
            Integer selectedCount,
            String itemSelectedRate) {
        this.itemSequenceNumber = itemSequenceNumber;
        this.item = item;
        this.itemType = itemType;
        this.itemTypeValue = itemTypeValue;
        this.selectedCount = selectedCount;
        this.itemSelectedRate = itemSelectedRate;
    }

    public QuestionSearchResponse setSelectStatistics(Integer selectedCount,
                                                      String itemSelectedRate) {
        return new QuestionSearchResponse(this.itemSequenceNumber,
                this.item, this.itemType, this.itemTypeValue, selectedCount, itemSelectedRate);
    }

}
