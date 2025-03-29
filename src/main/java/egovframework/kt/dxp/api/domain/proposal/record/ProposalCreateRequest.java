package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;

/**
 * 정책 제안 추가 요청
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@Schema(description = "정책 제안 추가 요청")
@Builder
public record ProposalCreateRequest(
        @Schema(description = "제안 순번 / int(11)", example = "1")
        @Hidden
        Long proposalSequenceNumber,
        @Schema(description = "제안 구분 / varchar(3)", example = "")
        @NotNull
        String proposalDivision,
        @Schema(description = "제목 / varchar(100)", example = "")
        @NotNull
        String title,
        @Schema(description = "배경 내용 / text", example = "")
        @NotNull
        String backgroundContents,
        @Schema(description = "제안 내용 / text", example = "")
        @NotNull
        String proposalContents,
        @Schema(description = "기대 효과 / text", example = "")
        @NotNull
        String expectEffect) {

    public ProposalCreateRequest setProposalSequenceNumber(
            Long proposalSequenceNumber) {
        return new ProposalCreateRequest(proposalSequenceNumber, this.proposalDivision,
                this.title, this.backgroundContents, this.proposalContents, this.expectEffect);
    }
}
