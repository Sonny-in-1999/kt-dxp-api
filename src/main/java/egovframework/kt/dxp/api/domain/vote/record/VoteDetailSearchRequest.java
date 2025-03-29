package egovframework.kt.dxp.api.domain.vote.record;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 투표 상세 조회 요청
 *
 * @author BITNA
 * @since 2024-11-01<br />
 */
@Schema(description = "투표 상세 조회 요청")
public record VoteDetailSearchRequest(
        @Schema(description = "투표 순번 / int(11)", example = "1")
        Integer voteSequenceNumber) {
}
