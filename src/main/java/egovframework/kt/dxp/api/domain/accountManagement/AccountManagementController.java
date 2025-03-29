package egovframework.kt.dxp.api.domain.accountManagement;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountInformationSearchResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementModifyRequest;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementModifyResponse;
import egovframework.kt.dxp.api.domain.accountManagement.record.AccountManagementSearchResponse;
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
 * Filename      : AccountManagementController
 * Description   :  계정정보 관리 Controller
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 *                 2 - 2024-10-28, BITNA, 계정정보 조회 추가
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MYP] 계정관리", description = "[담당자:  MINJI]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    @PostMapping(value = "/v1/accountinformation/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-006] 계정 정보 조회")
    public ResponseEntity<ItemResponse<AccountInformationSearchResponse>> getSearchAccountInformation() {
        return ResponseEntity.ok()
                             .body(accountManagementService.getSearchAccountInformation());
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/accountmanagement/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-007] 맞춤 정보 조회")
    public ResponseEntity<ItemResponse<AccountManagementSearchResponse>> getSearchAccountmanagemen() {
        return ResponseEntity.ok()
                .body(accountManagementService.getSearchAccountmanagemen());
    }

    @ApiOperation(value = "[CDA-MYP-008] 맞춤 정보 수정")
    @PostMapping(value = "/v1/accountmanagement/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponse<AccountManagementModifyResponse> modifyAccountmanagement(@RequestBody @Valid AccountManagementModifyRequest parameter) {
        return accountManagementService.modifyAccountmanagement(parameter);
    }

}
