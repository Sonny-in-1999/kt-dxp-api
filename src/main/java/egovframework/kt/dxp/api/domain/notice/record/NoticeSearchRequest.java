package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지사항 조회 요청")
public record NoticeSearchRequest(
        @Schema(description = "Current page number", example = "0", defaultValue = "0")
        int pageNo,
        @Schema(description = "Number of data in page", example = "10", defaultValue = "10")
        int pageSize
) {
}
