package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 정책 상세 조회 응답
 *
 * @author BITNA
 * @apiNote 2024-10-31 BITNA DB 컬럼 변경 result -> answer
 * @since 2024-10-29<br />
 */
@Schema(description = "정책 상세 조회 응답")
public record ProposalDetailSearchResponse(
        @ApiModelProperty(name = "제안 순번 / int(11)", example = "1")
        Long proposalSequenceNumber,
        @ApiModelProperty(name = "생성 일시 / datetime", example = "")
        String createDate,
        @ApiModelProperty(name = "수정 일시(검토완료시 사용) / datetime", example = "")
        String updateDate,
        @ApiModelProperty(name = "제안 구분 / varchar(3)", example = "")
        String proposalDivision,
        @ApiModelProperty(name = "제안 진행 구분 / varchar(3)", example = "")
        String proposalProgressDivision,
        @ApiModelProperty(name = "제목 / varchar(100)", example = "")
        String title,
        @ApiModelProperty(name = "배경 내용 / text", example = "")
        String backgroundContents,
        @ApiModelProperty(name = "제안 내용 / text", example = "")
        String proposalContents,
        @ApiModelProperty(name = "기대 효과 / text", example = "")
        String expectEffect,
        @ApiModelProperty(name = "생성자명", example = "")
        String createUserName,
        @ApiModelProperty(name = "답변", example = "")
        String answer,
        @ApiModelProperty(name = "답변 생성 날짜", example = "")
        String answerCreateDate,
        @ApiModelProperty(name = "답변 작성자", example = "")
        String answerCreateUserName,
        @ApiModelProperty(name = "삭제 권한 여부", example = "")
        String deleteAuthYn
) {

}
