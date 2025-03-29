package egovframework.kt.dxp.api.domain.vote.record;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


/**
 * 사용자 투표 추가 응답
 *
 * @author BITNA
 * @since 2024-11-04<br />
 */
@Schema(description = "사용자 투표 추가 요청")
public record VoteUserCreateResponse(
        @Schema(description = "투표 순번", example = "1")
        Integer voteSequenceNumber,
        @Schema(description = "투표 항목 순번 리스트", example = "1")
        List<Integer> itemSequenceNumberList) {
}
