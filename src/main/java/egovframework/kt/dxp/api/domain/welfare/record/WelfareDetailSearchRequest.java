package egovframework.kt.dxp.api.domain.welfare.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "복지 정책 상세 요청")
public record WelfareDetailSearchRequest(

        @Schema(name = "복지 순번", example = "1")
        Integer welfareSequenceNumber
) {
}
