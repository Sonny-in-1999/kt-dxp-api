package egovframework.kt.dxp.api.domain.citizen;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.citizen.record.CredentialData;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenBookmarkRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenDetailRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenManualRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenResponse;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStatusRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStatusResponse;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenVPRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenVpDataResponse;
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
 * Filename      : MobileCitizenController
 * Description   :  
 * Creation Date : 2024-12-11
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-12-11, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-DID] 모바일 신분증", description = "[담당자: Juyoung Chae]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MobileCitizenController {

    private final MobileCitizenService mobileCitizenService;

    @ApiOperation(value = "[CDA-DID-001] 신분증 발급 시작", notes = "신분증을 발급 시작 합니다.")
    @PostMapping(value = "/v1/did/start", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<MobileCitizenResponse>> startAppToApp(@RequestBody @Valid MobileCitizenRequest request)
            throws Exception {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.startApp2App(request));
    }

    @ApiOperation(value = "[CDA-DID-001] 신분증 발급 시작", notes = "신분증을 발급 시작 합니다.")
    @PostMapping(value = "/v1/did/manual/start", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> startManual(@RequestBody @Valid MobileCitizenManualRequest request)
            throws Exception {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.startManual(request));
    }

    @ApiOperation(value = "[CDA-DID-001] 신분증 발급", notes = "신분증을 발급합니다.")
    @PostMapping(value = "/v1/did/vpdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<MobileCitizenVpDataResponse>> vpData(@RequestBody @Valid MobileCitizenVPRequest request)
            throws Exception {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.vpSubmit(request));
    }

    @ApiOperation(value = "[CDA-DID-001] 신분증 목록 조회", notes = "신분증 목록을 조회합니다.")
    @PostMapping(value = "/v1/did/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemsResponse<CredentialData>> getSearchList() {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.getSearchList());
    }

    @ApiOperation(value = "[CDA-DID-001] 신분증 상세 조회", notes = "신분증 상세 조회합니다.")
    @PostMapping(value = "/v1/did/detail/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<CredentialData>> getSearchDetailList(@RequestBody @Valid MobileCitizenDetailRequest request) {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.getSearchDetailList(request));
    }

    @ApiOperation(value = "[CDA-DID-001] 신분증 삭제", notes = "신분증 삭제")
    @PostMapping(value = "/v1/did/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>>deleteDid(@RequestBody @Valid MobileCitizenDetailRequest request) {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.deleteDid(request));
    }

    @ApiOperation(value = "[CDA-DID-002] 신분증 상태 확인/관리", notes = "모바일 신분증 발급 여부를 확인하고 필요시 삭제합니다.")
    @PostMapping(value = "/v1/did/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<MobileCitizenStatusResponse>> manageDid(@RequestBody @Valid MobileCitizenStatusRequest request) {
        return ResponseEntity.ok()
                             .body(mobileCitizenService.manageDid(request));
    }

    @ApiOperation(value = "[CDA-DID-003] 신분증 즐겨찾기", notes = "모바일 신분증 즐겨찾기를 설정합니다.")
    @PostMapping(value = "/v1/did/bookmark/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Integer>> getModifyDidBookmark(@RequestBody @Valid MobileCitizenBookmarkRequest request) {
        return ResponseEntity.ok()
                .body(mobileCitizenService.getModifyDidBookmark(request));
    }


}
