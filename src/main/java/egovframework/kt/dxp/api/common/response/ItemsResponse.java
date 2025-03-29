package egovframework.kt.dxp.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * @author GEONLEE
 * @apiNote 2024-11-01 BITNA totalSize 추가
 * @since 2024-07-29
 */
@Builder
@Schema(description = "공통 복수 객체 응답 Record")
public record ItemsResponse<T>(
        @Schema(description = "응답 상태", example = "OK")
        String status,
        @Schema(description = "응답 상태 메시지", example = "데이터를 조회/추가/수정/삭제 하는데 성공하였습니다.")
        String message,
        @Schema(description = "복수 응답 객체")
        List<T> items,
        @Schema(description = "정상 응답 데이터 개수", example = "1")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long totalSize
) {
}
