package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "복지정책 링크 버튼 응답")
public record WelfareButtonSearchResponse(

        @Schema(name = "버튼 순번", example = "1")
        Integer buttonSequenceNumber,

        @Schema(name = "버튼 명", example = "복지 정책 링크 버튼 1")
        String buttonName,

        @Schema(name = "링크 URL", example = "링크 URL입니다.")
        String linkUniformResourceLocator
) {
}
