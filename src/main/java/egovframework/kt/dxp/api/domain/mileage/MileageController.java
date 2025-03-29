package egovframework.kt.dxp.api.domain.mileage;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.mileage.record.MileageSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 마일리지 Controller
 *
 * @author BITNA
 * @since 2024-12-02<br />
 */
@RestController
@Api(tags = "저탄소 마일리지 조회", description = "[담당자 : BITNA]")
@RequiredArgsConstructor
public class MileageController {
    private final MileageService mileageService;

    @PostMapping(value = "/v1/mileage/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "저탄소 마일리지 조회", notes = """
            """
    )
    public ResponseEntity<ItemResponse<MileageSearchResponse>> getMileage() {
        return ResponseEntity.ok()
                .body(mileageService.getMileage());
    }

}
