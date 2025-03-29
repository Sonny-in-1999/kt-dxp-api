package egovframework.kt.dxp.api.domain.welfare;

import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareCodeResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareDetailSearchRequest;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareDetailSearchResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareListSearchRequest;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareListSearchResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareComboResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "[CDA-SER-018~019] 복지 정책 조회", description = "[담당자 : Minsu Son]")
public class WelfareController {

    private final WelfareService welfareService;


    @ApiOperation(value = "[CDA-SER-018] 복지 정책 목록 조회")
    @PostMapping(value = "/v1/welfare/list/search"
            , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GridResponse<WelfareListSearchResponse>> getListSearchWelfare(
            @RequestBody WelfareListSearchRequest parameter) throws IOException {
        GridResponse<WelfareListSearchResponse> searchWelfareList
                = welfareService.getListSearchWelfare(parameter);
        return ResponseEntity.ok().body(searchWelfareList);
    }

    @ApiOperation(value = "[CDA-SER-018-001] 복지 정책 코드 목록 조회")
    @PostMapping(value = "/v1/welfare/list/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemsResponse<WelfareCodeResponse>> getListWelfareDivisionCode() {
        ItemsResponse<WelfareCodeResponse> codeList
                = welfareService.getWelfareDivisionCodeList();
        return ResponseEntity.ok().body(codeList);
    }

    @ApiOperation(value = "[CDA-SER-018-002] 복지 정책별 세부 분류 콤보 조회", notes = """
            - 현재는 RequestBody 필요 없이 생애주기별 세부 분류 콤보 목록만 반환합니다.
            - 추후 다른 복지 구분에도 세부 분류 추가될 경우 요청값이 변경될 수 있습니다.
            """)
    @PostMapping(value = "/v1/welfare/combo/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemsResponse<WelfareComboResponse>> getListWelfareCombo(
            // 다른 복지 구분에도 세부 분류 추가될 경우 Request 필요
    ) {
        ItemsResponse<WelfareComboResponse> comboList
                = welfareService.getWelfareComboList();
        return ResponseEntity.ok().body(comboList);
    }

    @ApiOperation(value = "[CDA-SER-019] 복지 정책 상세 조회")
    @PostMapping(value = "/v1/welfare/detail/search"
            , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<WelfareDetailSearchResponse>> getDetailSearchWelfare(
            @RequestBody WelfareDetailSearchRequest parameter) throws IOException {
        ItemResponse<WelfareDetailSearchResponse> searchWelfareDetail
                = welfareService.getSearchWelfareDetail(parameter);
        return ResponseEntity.ok().body(searchWelfareDetail);
    }

//    @ApiOperation(value = "[CDA-SER-019-001] 복지 정책 첨부파일 다운로드")
//    @PostMapping(value = "/v1/welfare/detail/file-download", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void getDownloadFile(
//            @RequestBody FileDownloadRequest parameter, HttpServletResponse httpServletResponse) {
//        welfareService.getDownloadFile(parameter, httpServletResponse);
//    }
}
