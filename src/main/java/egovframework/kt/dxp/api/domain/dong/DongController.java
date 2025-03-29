package egovframework.kt.dxp.api.domain.dong;

import egovframework.kt.dxp.api.common.request.DynamicRequest;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.dong.record.DongSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[CDA-DNG] 행정동 조회", description = "[담당자:  MINJI]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class DongController {

    private final DongService dongService;

    @PostMapping(value = "/v1/dong/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-DNG-001] 행정동 조회")
    public ResponseEntity<ItemsResponse<DongSearchResponse>> getSearchDongList(
            @RequestBody @Valid DynamicRequest parameter) {
        return ResponseEntity.ok()
                .body(dongService.getSearchDongList(parameter));
    }

}
