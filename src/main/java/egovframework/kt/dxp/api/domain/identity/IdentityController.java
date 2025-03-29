package egovframework.kt.dxp.api.domain.identity;

import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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
 * Filename      : IdentityController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MYP] 신분증 관리", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class IdentityController {
    //private final IdentityService identityService;

    @ApiOperation(value = "[CDA-MYP-002] 신분증 관리 추가", notes = "신분증 관리를 추가합니다.")
    @PostMapping(value = "/v1/identity/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<IdentityResponse>> createIdentity(@RequestBody @Valid IdentityRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createIdentity(@RequestBody @Valid DynamicRequest parameter) {
        //return identityService.createIdentity(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-002] 신분증 관리 수정", notes = "신분증 관리를 수정합니다.")
    @PostMapping(value = "/v1/identity/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<IdentityResponse>> modifyIdentity(@RequestBody @Valid IdentityRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyIdentity(@RequestBody @Valid DynamicRequest parameter) {
        //return identityService.modifyIdentity(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-002] 신분증 관리 삭제", notes = "신분증 관리를 삭제합니다.")
    @PostMapping(value = "/v1/identity/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<IdentityResponse>> deleteIdentity(@RequestBody @Valid IdentityRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteIdentity(@RequestBody @Valid DynamicRequest parameter) {
        //return identityService.deleteIdentity(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/identity/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-002] 신분증 관리 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<IdentityResponse>> getSearchIdentityList(
    public ResponseEntity<ItemsResponse<Long>> getSearchIdentityList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(identityService.getSearchIdentityList(request));
    }
}
