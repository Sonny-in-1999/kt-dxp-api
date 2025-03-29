package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 정책 제안 추가 응답
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@Schema(description = "정책 제안 추가 응답")
public record ProposalCreateResponse(
        @Schema(description = "제안 순번 / int(11)", example = "1")
        Long proposalSequenceNumber,
        @Schema(description = "생성 일시 / datetime", example = "")
        String createDate,
        @Schema(description = "제안 구분 / varchar(3)", example = "")
        String proposalDivision,
        @Schema(description = "제안 진행 구분 / varchar(3)", example = "")
        String proposalProgressDivision,
        @Schema(description = "제목 / varchar(100)", example = "")
        String title,
        @Schema(description = "배경 내용 / text", example = "")
        String backgroundContents,
        @Schema(description = "제안 내용 / text", example = "")
        String proposalContents,
        @Schema(description = "기대 효과 / text", example = "")
        String expectEffect,
        @Schema(description = "생성자 아이디 / varchar(36)", example = "")
        String createUserId) {

}
