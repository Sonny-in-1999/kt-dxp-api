package egovframework.kt.dxp.api.domain.main;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.main.popup.record.PopupCheckRequest;
import egovframework.kt.dxp.api.domain.main.popup.record.PopupSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.MainSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.PopupSearchRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[CDA-MAI] 메인", description = "[담당자: MINJI]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping(value = "/v1/main/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MAI-001] 메인 목록 조회")
    public ResponseEntity<ItemResponse<MainSearchResponse>> getSearchMainList() throws Exception {
        return ResponseEntity.ok()
                .body(mainService.getSearchMainList());
    }

    @PostMapping(value = "/v1/main/popup/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MAI-001] 팝업 조회")
    public ResponseEntity<ItemsResponse<PopupSearchResponse>> getSearchPopupList(@RequestBody @Valid PopupSearchRequest request) {
        return ResponseEntity.ok()
                .body(mainService.getSearchPopupList(request));
    }

    @PostMapping(value = "/v1/main/popup/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MAI-002] 팝업 조회 이력 확인")
    public ResponseEntity<ItemResponse<Boolean>> checkPopupView(@RequestBody @Valid PopupCheckRequest request) {
        return ResponseEntity.ok()
                             .body(mainService.checkPopupView(request));
    }
}
