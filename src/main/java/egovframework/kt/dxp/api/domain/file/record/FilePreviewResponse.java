package egovframework.kt.dxp.api.domain.file.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FilePreviewResponse(
        @Schema(description = "image Data to Byte", example = "")
        String imagePreview
) {
}
