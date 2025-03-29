package egovframework.kt.dxp.api.domain.nice.record;

import egovframework.kt.dxp.api.domain.user.enumeration.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@Builder
public record UserLoginResponse(
        @ApiModelProperty(value = "사용자 ID (UUID)")
        String userId,
        @ApiModelProperty(value = "사용자명", example = "홍길동")
        String userName,
        @ApiModelProperty(value = "휴대폰 번호", example = "01055559999")
        String mobilePhoneNumber,
        @ApiModelProperty(value = "14세 미만 여부(어린이 여부)", example = "false")
        Boolean isChildren,
        @ApiModelProperty(value = "생년월일", example = "20000101")
        String birthDate,
        @ApiModelProperty(value = "성별", example = "MAIL")
        Gender gender,
        @ApiModelProperty(value = "Access token")
        String accessToken,
        @ApiModelProperty(value = "Refresh token")
        String refreshToken
) {
}
