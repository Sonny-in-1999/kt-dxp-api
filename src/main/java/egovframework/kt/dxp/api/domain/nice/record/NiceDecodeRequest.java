package egovframework.kt.dxp.api.domain.nice.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@ApiModel(value = "NiceResDTO - Nice 인코딩 데이터 응답")
@Builder
public record NiceDecodeRequest(
        @ApiModelProperty(value = "Nice 로 부터 전달받은 인코딩된 암호화 된 데이터")
        String encodedData,
        @ApiModelProperty(value = "운영체제 유형", example = "IOS/AOS")
        String operatingSystemType,
        @ApiModelProperty(value = "푸시 키 / varchar(512)", example = "cno7vF36xTjpJxyL")
        String pushKey
) {
}
