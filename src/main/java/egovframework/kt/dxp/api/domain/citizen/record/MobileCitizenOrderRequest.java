package egovframework.kt.dxp.api.domain.citizen.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MobileCitizenOrderRequest(
        @Schema(description = "정렬")
        List<Order> order
) {
        public record Order(
        @ApiModelProperty(name = "거래코드", example = "2024122611404765991A79C31")
        String trxCode,
        @ApiModelProperty(name = "즐겨찾기 여부", example = "Y/N")
        UseYn bookmarkYn,
        @ApiModelProperty(name = "정렬 순서", example = "1")
        String displaySequence
        ){}
}
