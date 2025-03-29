package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/**
 * 이력 공지 사용자 추가 요청
 *
 * @author MINJI
 * @since 2024-10-17
 */
@Schema(description = "이력 공지 사용자 추가 요청")
public record NoticeDetailCreateRequest(
        @Schema(description = "사용자 아이디", example = "user1")
        @NotEmpty
        String userId,
        @Schema(description = "공지 순번", example = "1")
        @NotNull
        Integer noticeSequenceNumber
) {

}
