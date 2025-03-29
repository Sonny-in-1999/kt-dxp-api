package egovframework.kt.dxp.api.domain.terms.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 약관 조회 응답
 *
 * @author BITNA
 * @since 2024-10-18<br />
 */
@Schema(description = "약관 날짜 조회 응답")
public record TermsStartDateSearchResponse(
        @Schema(description = "약관 유형", example = "")
        String termsType,
        @Schema(description = "약관 시작 일시 리스트 / date", example = "")
        List<String> termsStartDateList) {

        @Builder
        public TermsStartDateSearchResponse (String termsType, List<String> termsStartDateList) {
                this.termsType = termsType;
                this.termsStartDateList = termsStartDateList;
        }
}
