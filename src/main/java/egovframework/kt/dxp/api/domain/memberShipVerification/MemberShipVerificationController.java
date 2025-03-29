package egovframework.kt.dxp.api.domain.memberShipVerification;

import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
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
 * Filename      : MembershipverificationController
 * Description   :
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-JOI] 회원 검증", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MemberShipVerificationController {
    //private final MembershipverificationService membershipVerificationService;

    @ApiOperation(value = "[CDA-JOI-003] 회원 검증 추가", notes = "회원 검증를 추가합니다.")
    @PostMapping(value = "/v1/membershipVerification/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipverificationResponse>> createMembershipverification(@RequestBody @Valid MembershipverificationRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createMembershipverification(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipVerificationService.createMembershipverification(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-JOI-003] 회원 검증 수정", notes = "회원 검증를 수정합니다.")
    @PostMapping(value = "/v1/membershipVerification/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipverificationResponse>> modifyMembershipverification(@RequestBody @Valid MembershipverificationRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyMembershipverification(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipVerificationService.modifyMembershipverification(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-JOI-003] 회원 검증 삭제", notes = "회원 검증를 삭제합니다.")
    @PostMapping(value = "/v1/membershipVerification/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipverificationResponse>> deleteMembershipverification(@RequestBody @Valid MembershipverificationRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteMembershipverification(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipVerificationService.deleteMembershipverification(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/membershipVerification/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-JOI-003] 회원 검증 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<MembershipverificationResponse>> getSearchMembershipverificationList(
    public ResponseEntity<ItemsResponse<Long>> getSearchMembershipverificationList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(membershipVerificationService.getSearchMembershipverificationList(request));
    }
}