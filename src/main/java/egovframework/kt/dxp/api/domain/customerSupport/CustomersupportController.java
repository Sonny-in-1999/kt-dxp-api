package egovframework.kt.dxp.api.domain.customerSupport;

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
 * Filename      : CustomersupportController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MYP] 고객센터", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class CustomersupportController {
    //private final CustomersupportService customerSupportService;

    @ApiOperation(value = "[CDA-MYP-010] 고객센터 추가", notes = "고객센터를 추가합니다.")
    @PostMapping(value = "/v1/customerSupport/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<CustomersupportResponse>> createCustomersupport(@RequestBody @Valid CustomersupportRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createCustomersupport(@RequestBody @Valid DynamicRequest parameter) {
        //return customerSupportService.createCustomersupport(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-010] 고객센터 수정", notes = "고객센터를 수정합니다.")
    @PostMapping(value = "/v1/customerSupport/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<CustomersupportResponse>> modifyCustomersupport(@RequestBody @Valid CustomersupportRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyCustomersupport(@RequestBody @Valid DynamicRequest parameter) {
        //return customerSupportService.modifyCustomersupport(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-010] 고객센터 삭제", notes = "고객센터를 삭제합니다.")
    @PostMapping(value = "/v1/customerSupport/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<CustomersupportResponse>> deleteCustomersupport(@RequestBody @Valid CustomersupportRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteCustomersupport(@RequestBody @Valid DynamicRequest parameter) {
        //return customerSupportService.deleteCustomersupport(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/customerSupport/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-010] 고객센터 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<CustomersupportResponse>> getSearchCustomersupportList(
    public ResponseEntity<ItemsResponse<Long>> getSearchCustomersupportList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(customerSupportService.getSearchCustomersupportList(request));
    }
}
