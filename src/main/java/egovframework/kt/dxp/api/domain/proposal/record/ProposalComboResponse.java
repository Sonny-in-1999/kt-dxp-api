package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 정책 제안 조회 응답
 *
 * @author BITNA
 * @since 2024-10-30<br />
 */
@Schema(description = "정책제안 구분 코드 콤보 응답")
public record ProposalComboResponse(
        @Schema(description = "제안 구분 코드 / varchar(3)", example = "01")
        String proposalDivision,
        @Schema(description = "제안 구분명 / varchar(3)", example = "복지")
        String proposalDivisionName
) {

}
