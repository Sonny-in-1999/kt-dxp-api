package egovframework.kt.dxp.api.domain.proposal;

import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.proposal.record.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 정책 제안 Controller
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@RestController
@Api(tags = "[CDA-SER-001~005] 정책 제안 정보 조회, 편집", description = "[담당자 : BITNA]")
@RequiredArgsConstructor
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping(value = "/v1/proposal/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-003] 정책 제안 정보 조회", notes = """
            # Parameters
             - proposalSequenceNumber [제안 순번]
             - createDate [생성 일시]
             - proposalDivision [제안 구분] => 조회 가능
             - proposalProgressDivision [제안 진행 구분] => 조회가능
             - title [제목]
             - createUserName [생성자명]
             """)
    public ResponseEntity<ItemsResponse<ProposalSearchResponse>> getProposalList(@RequestBody DynamicRequest parameter) {
        return ResponseEntity.ok()
                .body(proposalService.getProposalList(parameter));
    }

    @PostMapping(value = "/v1/proposal/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-004] 정책 제안 정보 조회", notes = """
            # Response
             - proposalSequenceNumber [제안 순번]
             - createDate [생성 일시]
             - updateDate [수정 일시] => 검토완료 일시
             - proposalDivision [제안 구분]
             - proposalProgressDivision [제안 진행 구분]
             - title [제목]
             - backgroundContents [배경 내용]
             - proposalContents [제안 내용]
             - expectEffect [기대 효과]
             - result [결과]
             - createUserName [생성자명]
             """)
    public ResponseEntity<ItemResponse<ProposalDetailSearchResponse>> getProposalDetail(@RequestBody @Valid ProposalSearchRequest parameter) {
        return ResponseEntity.ok()
                .body(proposalService.getProposalDetail(parameter));
    }

    @PostMapping(value = "/v1/proposal/combo/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-003] 정책 제안 콤보 조회", notes = """
            # Response
             - proposalDivision [제안 구분 코드]
             - proposalDivisionName [제안 구분 코드명]
             """)
    public ResponseEntity<ItemsResponse<ProposalComboResponse>> getProposalDivisionCombo() {
        return ResponseEntity.ok()
                .body(proposalService.getProposalDivisionCombo());
    }

    @PostMapping(value = "/v1/proposal/image/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-004] 정책 제안 이미지 조회", notes = """
            # Response
             - fileName [파일명]
             - fileExtension [파일 확장자]
             - image [base64 image] 
             """)
    public ResponseEntity<ItemsResponse<ProposalFileSearchResponse>> getProposalFile(@RequestBody @Valid ProposalSearchRequest parameter) {
        return ResponseEntity.ok()
                .body(proposalService.getProposalFile(parameter));
    }


    @PostMapping(value = "/v1/proposal/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-001] 정책 제안 추가", notes = """
            # Parameters
             - proposalDivision [제안 구분]
             - title [제목]
             - backgroundContents [배경 내용]
             - proposalContents [제안 내용]
             - expectEffect [기대 효과]
             """)
    public ResponseEntity<ItemResponse<ProposalCreateResponse>> createProposal(@RequestPart @Valid ProposalCreateRequest parameter,
                                                                               @RequestParam(required = false) List<MultipartFile> fileList) {
        return ResponseEntity.ok()
                .body(proposalService.createProposal(parameter, fileList));
    }

    @PostMapping(value = "/v1/proposal/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-002] 정책 제안 삭제", notes = """
            # Parameters
            - proposalSequenceNumber [제안 순번]
             """)
    public ResponseEntity<ItemResponse<Long>> deleteProposal(@RequestBody @Valid ProposalDeleteRequest parameter) {
        return ResponseEntity.ok()
                .body(proposalService.deleteProposal(parameter));
    }
}
