package egovframework.kt.dxp.api.domain.vote.record;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;


/**
 * 투표 상세 조회 요청
 *
 * @author BITNA
 * @since 2024-11-01<br />
 */
@Schema(description = "투표 상세 조회 요청")
public record VoteSearchRequest(
        @Schema(description = "page 순번 / int(11)", example = "1")
        Integer pageNo,
        @Schema(description = "page 사이즈 / int(11)", example = "1")
        Integer pageSize,
        @Schema(description = "투표 진행/종료 구분 - 전체[01], 진행[02], 종료[03] / int(11)", example = "1")
        @NotNull
        String progressEndDivision,
        @Schema(description = "사용자 투표 구분 - 전체[01], 참여가능[02], 참여불가능[03], 참여완료[04] / int(11)", example = "1")
        @NotNull
        String userVoteDivision) {
}
