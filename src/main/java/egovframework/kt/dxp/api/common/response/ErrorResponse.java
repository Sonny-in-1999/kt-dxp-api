package egovframework.kt.dxp.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-07-30
 */
@Builder
@ApiModel(description = "공통 응답 record")
public record ErrorResponse(
        String status,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String detailMessage
) {
}
