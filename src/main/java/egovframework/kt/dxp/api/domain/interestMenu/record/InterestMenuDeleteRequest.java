package egovframework.kt.dxp.api.domain.interestMenu.record;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : Interestmenu Delete Request
 * Description   : 관심메뉴 삭제 요청
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-23, MinJi Chae, 최초작성
 ******************************************************************************************/
public record InterestMenuDeleteRequest(
        @ApiModelProperty(name = "사용자 아이디", example = "user1", hidden = true)
        String userId,
        @ApiModelProperty(name = "메뉴 아이디", example = " [\n"
                + "    {\n"
                + "      \"menuId\": \"CCAM000001\"\n"
                + "    },\n"
                + "{\n"
                + "      \"menuId\": \"CCAM000002\"\n"
                + "    }\n"
                + "  ]\n")
        @NotEmpty
        List<MenuRequest> menuList
) {

}
