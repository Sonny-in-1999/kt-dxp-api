package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * 정책 제안 삭제 요청
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@Schema(description = "정책 제안 삭제 요청")
public record ProposalDeleteRequest(
        @Schema(description = "제안 순번 / int(11)", example = "1")
        @NotNull
        Long proposalSequenceNumber) {

}
