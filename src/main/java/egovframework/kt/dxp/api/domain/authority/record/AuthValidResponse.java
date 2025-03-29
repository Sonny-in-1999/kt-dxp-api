package egovframework.kt.dxp.api.domain.authority.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record AuthValidResponse(
        @ApiModelProperty(value = "사용자 아이디")
        String userId,
        @ApiModelProperty(value = "사용자 명")
        String userName,
        @ApiModelProperty(value = "생년월일")
        String birthDate,
        @ApiModelProperty(value = "성별", example = "MAIL")
        String gender,
        @ApiModelProperty(value = "14세 미만 여부(어린이 여부)")
        Boolean isChildren,
        @ApiModelProperty(value = "휴대폰 번호")
        String mobilePhoneNumber
)implements
        Serializable {
}