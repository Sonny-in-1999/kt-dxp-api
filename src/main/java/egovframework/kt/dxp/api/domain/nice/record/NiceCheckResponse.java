package egovframework.kt.dxp.api.domain.nice.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@ApiModel(value = "Nice 인코딩 데이터 응답")
@Builder
public record NiceCheckResponse(
        @ApiModelProperty(value = "Nice 로 부터 전달받은 응답 코드", example = "OK")
        String code,
        @ApiModelProperty(value = "응답 메시지", example = "인코딩 정보 생성에 성공하였습니다.")
        String message,
        @ApiModelProperty(value = "Nice 로 부터 전달받은 인코딩된 암호화 데이터")
        String cipherData
) {
}
