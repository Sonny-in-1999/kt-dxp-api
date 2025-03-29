package egovframework.kt.dxp.api.domain.nice.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@Builder
public record LoginCheckResponse(
        @ApiModelProperty(value = "로그인 응답 코드", example = "LG_NR")
        String code,
        @ApiModelProperty(value = "로그인 응답 메시지", example = "회원가입하지 않은 사용자 입니다.")
        String message,
        @ApiModelProperty(value = "사용자 로그인 응답 객체")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UserLoginResponse userLoginResponse
) {
}
