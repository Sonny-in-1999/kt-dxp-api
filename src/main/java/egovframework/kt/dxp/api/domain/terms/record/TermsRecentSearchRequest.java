package egovframework.kt.dxp.api.domain.terms.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 약관 조회 요청
 *
 * @author BITNA
 * @since 2024-11-05<br />
 */
@Schema(description = "최신 약관 조회 요청")
@Builder
public record TermsRecentSearchRequest(
        @Schema(description = "약관 유형", example = "")
        String termsType
        ) {

}
