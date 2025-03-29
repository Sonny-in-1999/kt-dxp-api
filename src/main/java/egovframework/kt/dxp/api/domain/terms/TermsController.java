package egovframework.kt.dxp.api.domain.terms;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.terms.record.*;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 약관 Controller
 *
 * @author BITNA
 * @apiNote 2024-11-05 BITNA 약관 유형별로 조회가능하도록 변경
 * 2024-11-06 BITNA 약관 유형/날짜 별로 조회 가능하도록 변경
 * @since 2024-10-18<br />
 */
@RestController
@Api(tags = "[CDA-MYP] 약관 정보 조회", description = "[담당자 : BITNA]")
@RequiredArgsConstructor
public class TermsController {
    private final TermsService termsService;

    @PostMapping(value = "/v1/terms/type/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "약관 유형 조회", description = """
            """,
            operationId = "[CDA-MYP-014]"
    )
    public ResponseEntity<ItemsResponse<TermsGroupSearchResponse>> getTermsTypeList() {
        return ResponseEntity.ok()
                .body(termsService.getTermsTypeList());
    }

    @PostMapping(value = "/v1/terms/date/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "약관 정보 날짜 조회", description = """
            # Parameters
            - termsType [약관 유형]
            """,
            operationId = "[CDA-MYP-014]"
    )
    public ResponseEntity<ItemResponse<TermsStartDateSearchResponse>> getTermsStartDateList(@RequestBody TermsStartDateSearchRequest parameter) {
        return ResponseEntity.ok()
                .body(termsService.getTermsStartDateList(parameter));
    }

    @PostMapping(value = "/v1/terms/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "약관 정보 조회", description = """
            # Parameters
            - termsType [약관 유형]
            - termsStartDate [약관 시작 일시]
            """,
            operationId = "[CDA-MYP-014]"
    )
    public ResponseEntity<ItemResponse<TermsSearchResponse>> getTerms(@RequestBody TermsSearchRequest parameter) {
        return ResponseEntity.ok()
                .body(termsService.getTerms(parameter));
    }

    @PostMapping(value = "/v1/terms/recent/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "약관 최신 정보 조회", description = """
            # Parameters
            - termsType [약관 유형]
            """,
            operationId = "[CDA-MYP-014]"
    )
    public ResponseEntity<ItemResponse<TermsSearchResponse>> getRecentTerms(@RequestBody TermsSearchRequest parameter) {
        return ResponseEntity.ok()
                .body(termsService.getRecentTerms(parameter));
    }
}
