package egovframework.kt.dxp.api.domain.terms.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 약관 조회 응답
 *
 * @author BITNA
 * @apiNote 2024-11-05 BITNA 약관 유형별로 조회가능하도록 변경
 * 2024-11-06 BITNA cd테이블로 변경 후 필수 여부 추가
 * @since 2024-10-18<br />
 */
@Schema(description = "약관 조회 응답")
@Builder
public record TermsGroupSearchResponse(
        @Schema(description = "약관 유형", example = "")
        String termsType,
        @Schema(description = "약관 유형명", example = "")
        String termsTypeName,
        @Schema(description = "필수 여부 / varchar(1)", example = "")
        String essentialYn
        ) {

}
