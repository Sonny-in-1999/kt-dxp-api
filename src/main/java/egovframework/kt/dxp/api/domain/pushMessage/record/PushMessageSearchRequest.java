package egovframework.kt.dxp.api.domain.pushMessage.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 조회 요청")
public record PushMessageSearchRequest(
        @Schema(description = "Current page number", example = "0", defaultValue = "0")
        int pageNo,
        @Schema(description = "Number of data in page", example = "10", defaultValue = "10")
        int pageSize
) {
}
