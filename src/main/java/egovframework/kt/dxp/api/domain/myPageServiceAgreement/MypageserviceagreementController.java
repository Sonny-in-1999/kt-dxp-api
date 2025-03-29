package egovframework.kt.dxp.api.domain.myPageServiceAgreement;

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
 * Filename      : MypageserviceagreementController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-MYP] 서비스 약관", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MypageserviceagreementController {
    //private final MypageserviceagreementService myPageServiceAgreementService;

    @ApiOperation(value = "[CDA-MYP-014] 서비스 약관 추가", notes = "서비스 약관를 추가합니다.")
    @PostMapping(value = "/v1/myPageServiceAgreement/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MypageserviceagreementResponse>> createMypageserviceagreement(@RequestBody @Valid MypageserviceagreementRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createMypageserviceagreement(@RequestBody @Valid DynamicRequest parameter) {
        //return myPageServiceAgreementService.createMypageserviceagreement(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-014] 서비스 약관 수정", notes = "서비스 약관를 수정합니다.")
    @PostMapping(value = "/v1/myPageServiceAgreement/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MypageserviceagreementResponse>> modifyMypageserviceagreement(@RequestBody @Valid MypageserviceagreementRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyMypageserviceagreement(@RequestBody @Valid DynamicRequest parameter) {
        //return myPageServiceAgreementService.modifyMypageserviceagreement(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-MYP-014] 서비스 약관 삭제", notes = "서비스 약관를 삭제합니다.")
    @PostMapping(value = "/v1/myPageServiceAgreement/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MypageserviceagreementResponse>> deleteMypageserviceagreement(@RequestBody @Valid MypageserviceagreementRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteMypageserviceagreement(@RequestBody @Valid DynamicRequest parameter) {
        //return myPageServiceAgreementService.deleteMypageserviceagreement(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/myPageServiceAgreement/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-MYP-014] 서비스 약관 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<MypageserviceagreementResponse>> getSearchMypageserviceagreementList(
    public ResponseEntity<ItemsResponse<Long>> getSearchMypageserviceagreementList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(myPageServiceAgreementService.getSearchMypageserviceagreementList(request));
    }
}
