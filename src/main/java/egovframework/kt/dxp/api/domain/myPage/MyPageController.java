package egovframework.kt.dxp.api.domain.myPage;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.myPage.record.MyPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : MyPageController
 * Description   : 마이페이지 Controller
 * Creation Date : 2024-10-15
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-21, MinJi Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MYP] 마이페이지", description = "[담당자: MINJI]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @PostMapping(value = "/v1/mypage/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-001] 마이페이지 목록 조회")
    public ResponseEntity<ItemResponse<MyPageResponse>> getSearchMyPageList() {
        return ResponseEntity.ok()
                .body(myPageService.getSearchMyPageList());
    }
}
