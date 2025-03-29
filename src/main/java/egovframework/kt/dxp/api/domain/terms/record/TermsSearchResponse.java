package egovframework.kt.dxp.api.domain.terms.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 약관 조회 응답
 *
 * @author BITNA
 * @since 2024-10-18<br />
 */
@Schema(description = "약관 조회 응답")
public record TermsSearchResponse(
        @Schema(description = "약관 유형 / varchar(3)", example = "")
        String termsType,
        @Schema(description = "약관 시작 일시 / date", example = "")
        String termsStartDate,
        @Schema(description = "내용 / text", example = "")
        String contents,
        @Schema(description = "정렬 순번 / int(2)", example = "1")
        Long sortSequence,
        @Schema(description = "필수 여부 / varchar(1)", example = "")
        UseYn essentialYn,
        @Schema(description = "사용 여부 / varchar(1)", example = "")
        UseYn useYn,
        @Schema(description = "생성 일시 / datetime", example = "")
        String createDate,
        @Schema(description = "수정 일시 / datetime", example = "")
        String updateDate) {

}
