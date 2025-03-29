package egovframework.kt.dxp.api.domain.interestMenu;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuCreateRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuDeleteRequest;
import egovframework.kt.dxp.api.domain.interestMenu.record.InterestMenuResponse;
import egovframework.kt.dxp.api.domain.interestMenu.record.MenuSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : InterestMenuController
 * Description   : 관심메뉴 Controller
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MAI] 관심메뉴", description = "[담당자: MINJI]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class InterestMenuController {

    private final InterestMenuService interestMenuService;

    @PostMapping(value = "/v1/menu/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MAI-002] 메뉴 목록 조회")
    public ResponseEntity<ItemsResponse<MenuSearchResponse>> getSearchMenuList() {
        return ResponseEntity.ok()
                .body(interestMenuService.getSearchMenuList());
    }

    @PostMapping(value = "/v1/interestmenu/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MAI-003] 관심메뉴 목록 조회")
    public ResponseEntity<ItemsResponse<InterestMenuResponse>> getSearchInterestMenuList() {
        return ResponseEntity.ok()
                .body(interestMenuService.getSearchInterestMenuList());
    }

    @ApiOperation(value = "[CDA-MAI-004] 관심메뉴 추가")
    @PostMapping(value = "/v1/interestmenu/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Integer>> createInterestMenu(
            @RequestBody @Valid InterestMenuCreateRequest parameter) {
        return ResponseEntity.ok().body(interestMenuService.createInterestMenu(parameter));
    }

    @ApiOperation(value = "[CDA-MAI-005] 관심메뉴 삭제")
    @PostMapping(value = "/v1/interestmenu/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Integer>> deleteInterestMenu(
            @RequestBody @Valid InterestMenuDeleteRequest parameter) {
        return ResponseEntity.ok().body(interestMenuService.deleteInterestMenu(parameter));

    }
}
