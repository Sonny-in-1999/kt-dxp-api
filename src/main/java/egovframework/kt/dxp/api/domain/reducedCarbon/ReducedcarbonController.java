package egovframework.kt.dxp.api.domain.reducedCarbon;

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
 * Filename      : ReducedcarbonController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-ACT] 저탄소 활동", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class ReducedcarbonController {
    //private final ReducedcarbonService reducedCarbonService;

    @ApiOperation(value = "[CDA-ACT-001] 저탄소 활동 추가", notes = "저탄소 활동를 추가합니다.")
    @PostMapping(value = "/v1/reducedCarbon/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<ReducedcarbonResponse>> createReducedcarbon(@RequestBody @Valid ReducedcarbonRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createReducedcarbon(@RequestBody @Valid DynamicRequest parameter) {
        //return reducedCarbonService.createReducedcarbon(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-ACT-001] 저탄소 활동 수정", notes = "저탄소 활동를 수정합니다.")
    @PostMapping(value = "/v1/reducedCarbon/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<ReducedcarbonResponse>> modifyReducedcarbon(@RequestBody @Valid ReducedcarbonRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyReducedcarbon(@RequestBody @Valid DynamicRequest parameter) {
        //return reducedCarbonService.modifyReducedcarbon(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-ACT-001] 저탄소 활동 삭제", notes = "저탄소 활동를 삭제합니다.")
    @PostMapping(value = "/v1/reducedCarbon/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<ReducedcarbonResponse>> deleteReducedcarbon(@RequestBody @Valid ReducedcarbonRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteReducedcarbon(@RequestBody @Valid DynamicRequest parameter) {
        //return reducedCarbonService.deleteReducedcarbon(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/reducedCarbon/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-ACT-001] 저탄소 활동 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<ReducedcarbonResponse>> getSearchReducedcarbonList(
    public ResponseEntity<ItemsResponse<Long>> getSearchReducedcarbonList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(reducedCarbonService.getSearchReducedcarbonList(request));
    }
}
