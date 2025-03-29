package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "복지 정책 구분 코드 응답")
public record WelfareCodeResponse(
        @Schema(description = "구분 코드", example = "01")
        String welfareDivision,

        @Schema(description = "구분 명", example = "생애주기별")
        String welfareDivisionName
) {
}
