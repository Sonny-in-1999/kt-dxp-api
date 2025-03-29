package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(name = "복지 정책 목록 요청")
public record WelfareListSearchRequest(

        @Schema(name = "복지 구분", example = "01")
        @Nullable
        String welfareDivision,

        @Schema(name = "복지 상세 구분", example = "01")
        @Nullable
        String welfareDetailDivision,

        @Schema(name = "페이지 번호", example = "0")
        @NotNull
        @Min(0)
        Integer pageNo,

        @Schema(name = "페이지 크기", example = "10")
        @NotNull
        @Min(1)
        Integer pageSize
) {
}
