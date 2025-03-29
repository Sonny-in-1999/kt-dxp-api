package egovframework.kt.dxp.api.domain.vote.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


/**
 * 투표 조회 요청
 *
 * @author BITNA
 * @since 2024-11-01<br />
 */
@Schema(description = "투표 조회 요청")
public record VoteItemSearchResponse(
        @Schema(description = "항목 순번", example = "1")
        Integer itemSequenceNumber,
        @Schema(description = "항목 내용", example = "")
        String item,
        @Schema(description = "항목 선택 여부", example = "")
        UseYn itemSelectYn,
        @Schema(description = "항목 선택 수", example = "")
        Integer selectedCount,
        @Schema(description = "항목 선택 비율", example = "")
        String itemSelectedRate) {

    @Builder
    public VoteItemSearchResponse(Integer itemSequenceNumber,
                                  String item,
                                  UseYn itemSelectYn,
                                  Integer selectedCount,
                                      String itemSelectedRate) {
        this.itemSequenceNumber = itemSequenceNumber;
        this.item = item;
        this.itemSelectYn = itemSelectYn;
        this.selectedCount = selectedCount;
        this.itemSelectedRate = itemSelectedRate;
    }

    public VoteItemSearchResponse setItemSelectYn(UseYn itemSelectYn) {
        return new VoteItemSearchResponse(this.itemSequenceNumber,
                this.item, itemSelectYn, this.selectedCount, this.itemSelectedRate);
    }

    public VoteItemSearchResponse setSelectStatistics(Integer selectedCount, String itemSelectedRate) {
        return new VoteItemSearchResponse(this.itemSequenceNumber,
                this.item, this.itemSelectYn, selectedCount, itemSelectedRate);
    }
}
