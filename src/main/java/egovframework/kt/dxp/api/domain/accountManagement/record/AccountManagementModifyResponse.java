package egovframework.kt.dxp.api.domain.accountManagement.record;

import io.swagger.v3.oas.annotations.media.Schema;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Accountmanagement Response
 * Description   :  계정정보 관리 조회 응답
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Schema(name = "계정정보 수정 응답")
public record AccountManagementModifyResponse(
        @Schema(description = "거주지", example = "신북읍")
        String dongCodeName,
        @Schema(description = "자녀수", example = "2")
        Integer childrenCount
) {

}