package egovframework.kt.dxp.api.domain.memberShipWithDrawal;

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
 * Filename      : MembershipwithdrawalController
 * Description   :
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-JOI] 회원 탈퇴", description = "[담당자: 미배정]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class MemberShipWithdrawalController {
    //private final MembershipwithdrawalService membershipWithdrawalService;

    @ApiOperation(value = "[CDA-JOI-004] 회원 탈퇴 추가", notes = "회원 탈퇴를 추가합니다.")
    @PostMapping(value = "/v1/membershipWithdrawal/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipwithdrawalResponse>> createMembershipwithdrawal(@RequestBody @Valid MembershipwithdrawalRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> createMembershipwithdrawal(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipWithdrawalService.createMembershipwithdrawal(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-JOI-004] 회원 탈퇴 수정", notes = "회원 탈퇴를 수정합니다.")
    @PostMapping(value = "/v1/membershipWithdrawal/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipwithdrawalResponse>> modifyMembershipwithdrawal(@RequestBody @Valid MembershipwithdrawalRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> modifyMembershipwithdrawal(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipWithdrawalService.modifyMembershipwithdrawal(parameter);
        return null;
    }

    @ApiOperation(value = "[CDA-JOI-004] 회원 탈퇴 삭제", notes = "회원 탈퇴를 삭제합니다.")
    @PostMapping(value = "/v1/membershipWithdrawal/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ItemResponse<MembershipwithdrawalResponse>> deleteMembershipwithdrawal(@RequestBody @Valid MembershipwithdrawalRequest parameter) {
    public ResponseEntity<ItemResponse<Long>> deleteMembershipwithdrawal(@RequestBody @Valid DynamicRequest parameter) {
        //return membershipWithdrawalService.deleteMembershipwithdrawal(parameter);
        return null;
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/membershipWithdrawal/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-JOI-004] 회원 탈퇴 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    // public ResponseEntity<ItemsResponse<MembershipwithdrawalResponse>> getSearchMembershipwithdrawalList(
    public ResponseEntity<ItemsResponse<Long>> getSearchMembershipwithdrawalList(
            @RequestBody DynamicRequest request) {
        return null;
        //return ResponseEntity.ok()
        //                     .body(membershipWithdrawalService.getSearchMembershipwithdrawalList(request));
    }
}