package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "복지 정책 상세 구분 코드 콤보 응답")
public record WelfareComboResponse(

        @Schema(description = "상세 구분 코드", example = "02")
        String welfareDetailDivision,

        @Schema(description = "상세 구분 명", example = "영유아")
        String welfareDetailDivisionName
) {
}
