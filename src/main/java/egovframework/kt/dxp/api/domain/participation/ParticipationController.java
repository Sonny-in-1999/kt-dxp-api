package egovframework.kt.dxp.api.domain.participation;

import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.domain.participation.record.ParticipationSearchRequest;
import egovframework.kt.dxp.api.domain.participation.record.ParticipationSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "[CDA-SER] 참여내역", description = "[담당자 : Juyoung Chae]")
@RequiredArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;

    @PostMapping(value = "/v1/participation/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-SER-020] 참여내역 정보 조회", notes = "참여내역 정보를 조회합니다.")
    public ResponseEntity<GridResponse<ParticipationSearchResponse>> getParticipationList(
            @Valid @RequestBody ParticipationSearchRequest participationSearchRequest) {
        return ResponseEntity.ok()
                             .body(participationService.getParticipationList(participationSearchRequest));
    }
}