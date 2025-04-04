package egovframework.kt.dxp.api.common.request;

import egovframework.kt.dxp.api.common.request.enumeration.Operator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Dynamic filter
 *
 * @author GEONLEE
 * @since 2024-04-09
 */
@Schema(description = "DynamicFilter")
@Builder
public record DynamicFilter(
        @Schema(description = "Field for Search", example = "field")
        String field,
        @Schema(description = "Search operator [eq, contains, between, in, etc...]", example = "eq")
        Operator operator,
        @Schema(description = "Value for search", example = "value")
        String value) {
}
