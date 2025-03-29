package egovframework.kt.dxp.api.domain.participation.record;

import egovframework.kt.dxp.api.domain.participation.model.ParticipationListImpl;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "참여내역 조회 응답")
public record ParticipationSearchResponse(
        @ApiModelProperty(name = "정책 결과 및 종료된 여부 Y/N", example = "Y")
        UseYn hasRedBadgeProposal,
        @ApiModelProperty(name = "투표 결과 및 종료된 여부 Y/N", example = "N")
        UseYn hasRedBadgeVote,
        @ApiModelProperty(name = "설문 결과 및 종료된 여부 Y/N", example = "N")
        UseYn hasRedBadgeSurvey,
        @ApiModelProperty(name = "목록 리스트", example = "divisionName, sequenceNumber, status, title, hasRedBadge, period")
        List<ParticipationListImpl> list
        ) {
}
//[Request]
//        {
//searchDivision, // ALL, Proposal, Vote, Survey
//pageNo,
//pageSize
//}
//        [Response]
//        {
//        "items": {
//        "hasRedBadgeProposal": "Y",
//        "hasRedBadgeVote": "N",
//        "hasRedBadgeSurvey": "N",
//        "list": [
//        {
//        "divisionName": "정책 제안",
//        "sequenceNumber": 1,
//        "status": "제안 접수",
//        "title": "청년 주거 비용 복지 제안합니다.",
//        "hasRedBadge": "Y", // 결과 및 종료된 여부 Y/N
//        "period": "2024.10.10 ~ YYYY.MM.DD" // 기간
//
//        }
//        ]
//        },
//        "message": "데이터를 조회하는데 성공하였습니다.",
//        "size": 1,
//        "status": "OK",
//        "totalPageSize": 1,
//        "totalSize": 1
//        }