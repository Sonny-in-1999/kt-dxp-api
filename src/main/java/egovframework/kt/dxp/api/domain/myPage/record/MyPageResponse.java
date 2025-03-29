package egovframework.kt.dxp.api.domain.myPage.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Mypage Response
 * Description   : 마이페이지 조회 응답
 * Creation Date : 2024-10-19
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-21, MinJi Chae, 최초작성
 ******************************************************************************************/
@Builder
public record MyPageResponse(
        //@ApiModelProperty(name = "사용자 명", example = "사용자1")
        //String userName,
        @ApiModelProperty(name = "정책 제안수", example = "2")
        Integer proposalCount,
        @ApiModelProperty(name = "투표 수", example = "3")
        Integer voteCount,
        @ApiModelProperty(name = "설문 참여수", example = "4")
        Integer surveyCount,
        @ApiModelProperty(name = "안 읽은 메세지 존재 여부", example = "N[없음], Y[있음]")
        UseYn pushMessageYn
        //@ApiModelProperty(name = "버전 정보", example = "1.0.0")
        //String versionInformation
) {

}
