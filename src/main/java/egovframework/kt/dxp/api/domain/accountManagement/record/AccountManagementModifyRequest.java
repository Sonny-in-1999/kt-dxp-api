package egovframework.kt.dxp.api.domain.accountManagement.record;

import io.swagger.v3.oas.annotations.media.Schema;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Accountmanagement Request
 * Description   :  계정정보 관리 수정 요청
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Schema(name = "계정정보 수정 요청")
public record AccountManagementModifyRequest(
        @Schema(description = "거주지", example = "10100")
        String dongCode,
        @Schema(description = "자녀수", example = "2")
        Integer childrenCount
) {

}
