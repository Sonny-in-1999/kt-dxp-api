package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 공지사항 상세 조회 요청
 *
 * @author MINJI
 * @since 2024-10-15
 */
@Schema(description = "공지사항 상세 조회 요청")
public record NoticeDetailSearchRequest(
        @Schema(description = "공지 순번", example = "1")
        @NotNull
        Integer noticeSequenceNumber
) {

}
