package egovframework.kt.dxp.api.domain.vote;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.vote.record.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : VoteController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 *                 2 - 2024-11-01, BITNA, 투표 목록/상세 조회, 사용자 투표 추가
 ******************************************************************************************/
@RestController
@Api(tags = "[CDA-SER-006~011] 투표 목록/상세 조회, 추가", description = "[담당자: BITNA]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class VoteUserController {
    private final VoteService voteService;

    @ApiOperation(value = "[CDA-SER-006] 투표 참여 추가", notes = "투표 참여를 추가합니다.")
    @PostMapping(value = "/v1/vote/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> createVote(@RequestBody @Valid VoteUserCreateRequest parameter) {
        return ResponseEntity.ok()
                .body(voteService.createVoteUser(parameter));
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/vote/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-008] 투표 참여 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            """)
    public ResponseEntity<ItemsResponse<VoteSearchResponse>> getSearchVoteList(
            @RequestBody VoteSearchRequest request) {
        return ResponseEntity.ok()
                .body(voteService.getSearchVoteList(request));
    }

    // 응답을 리스트 형식으로 여러개 전송할 때 사용
    @PostMapping(value = "/v1/vote/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-009] 투표 참여 상세 조회", notes = """
            # Searchable Field
            - voteSequenceNumber [투표 순번]
            """)
    public ResponseEntity<ItemResponse<VoteDetailSearchResponse>> getSearchVoteList(
            @RequestBody VoteDetailSearchRequest request) {
        return ResponseEntity.ok()
                .body(voteService.getSearchVoteDetail(request));
    }
}
