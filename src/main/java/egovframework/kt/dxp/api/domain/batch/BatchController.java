package egovframework.kt.dxp.api.domain.batch;

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
 * Filename      : BatchController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-COM] 배치", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class BatchController {
    //private final BatchService batchService;

    @ApiOperation(value = "[CDA-COM-001] 배치 추가", notes = "배치를 추가합니다.")
    @PostMapping(value = "/v1/batch/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<BatchResponse>> createBatch(@RequestBody @Valid BatchRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createBatch(@RequestBody @Valid DynamicRequest parameter) {
        //return batchService.createBatch(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-COM-001] 배치 수정", notes = "배치를 수정합니다.")
    @PostMapping(value = "/v1/batch/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<BatchResponse>> modifyBatch(@RequestBody @Valid BatchRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyBatch(@RequestBody @Valid DynamicRequest parameter) {
        //return batchService.modifyBatch(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-COM-001] 배치 삭제", notes = "배치를 삭제합니다.")
    @PostMapping(value = "/v1/batch/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<BatchResponse>> deleteBatch(@RequestBody @Valid BatchRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteBatch(@RequestBody @Valid DynamicRequest parameter) {
        //return batchService.deleteBatch(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/batch/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-COM-001] 배치 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<BatchResponse>> getSearchBatchList(
    public ResponseEntity<ItemsResponse<Long>> getSearchBatchList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(batchService.getSearchBatchList(request));
    }
}
