package egovframework.kt.dxp.api.domain.interestMenu.record;

import io.swagger.annotations.ApiModelProperty;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Interestmenu Create Response
 * Description   : 관심메뉴 추가 응답
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
public record InterestMenuCreateResponse(
        @ApiModelProperty(name = "메뉴 아이디", example = "MEN0000001")
        String menuId,
        @ApiModelProperty(name = "메뉴 명", example = "투표참여")
        String menuName
) {

}
