package egovframework.kt.dxp.api.domain.participation.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "참여내역 조회 요청")
public record ParticipationSearchRequest(
        @Schema(description = "조회 구분", example = "ALL, PROPOSAL, VOTE, SURVEY")
        String searchDivision,
        @Schema(description = "조회 서브 구분", example = "ALL, PROGRESS, COMPLETE")
        String searchSubDivision,
        @Schema(description = "Current page number", example = "0", defaultValue = "0")
        int pageNo,
        @Schema(description = "Number of data in page", example = "10", defaultValue = "10")
        int pageSize
        ) {
}
