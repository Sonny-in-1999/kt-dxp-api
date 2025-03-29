package egovframework.kt.dxp.api.domain.interestMenu.record;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Menu Request
 * Description   : 메뉴 요청
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
public record MenuRequest(
        @ApiModelProperty(name = "메뉴 아이디", example = "MEN0000004")
        @NotEmpty
        String menuId,
        @ApiModelProperty(name = "정렬 순번", example = "1")
        Integer sortSequenceNumber
) {

}
