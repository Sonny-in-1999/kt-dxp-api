package egovframework.kt.dxp.api.domain.vote.record;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 사용자 투표 추가 요청
 *
 * @author BITNA
 * @since 2024-11-04<br />
 */
@Schema(description = "사용자 투표 추가 요청")
public record VoteUserCreateRequest(
        @Schema(description = "투표 순번", example = "1")
        @NotNull
        Integer voteSequenceNumber,
        @Schema(description = "투표 항목 순번 리스트", example = "1")
        @NotNull
        List<Integer> itemSequenceNumberList) {
}
