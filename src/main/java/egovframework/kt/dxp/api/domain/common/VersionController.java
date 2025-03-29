package egovframework.kt.dxp.api.domain.common;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.common.record.VersionRequest;
import egovframework.kt.dxp.api.domain.common.record.VersionResponse;
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
@Api(tags = "[CDA-COM] 앱 최신 버전 정보 조회 API", description = "[담당자: Minsu Son]")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;


    @ApiOperation(value = "[CDA-COM-002] 운영체제 별 앱 최신 정보 조회")
    @PostMapping(value = "/v1/version/latest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<VersionResponse>> getLatestVersionInfo(@RequestBody @Valid VersionRequest request) {
        ItemResponse<VersionResponse> latestVersionInfo = versionService.getLatestVersionInfo(request);
        return ResponseEntity.ok().body(latestVersionInfo);
    }
}
