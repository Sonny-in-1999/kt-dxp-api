package egovframework.kt.dxp.api.domain.signUp.record;

import egovframework.kt.dxp.api.entity.M_USR;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO for {@link M_USR}
 */
@Builder
@Schema(description = "회원 가입 추가 요청")
public record SignUpCustomRequest(
        /* 본인인증암호화 데이터     */
        @Schema(description = "휴대폰 번호", example = "0101234XXXX")
        @NotNull
        String mobilePhoneNumber,

        /* 본인인증암호화 데이터     */
        @Schema(description = "비밀번호", example = "AgIHTkNDRTY4N2xYT5")
        @NotNull
        String password,

        @Schema(description = "사용자 명", example = "김춘천")
        @NotNull
        String userName,

        @Schema(description = "생년월일", example = "19990101")
        @NotNull
        String birthDate,

        @Schema(description = "성별 유형", example = "M/F")
        @NotNull
        String genderType,

        @Schema(description = "운영체계 유형", example = "AOS/IOS")
        @NotNull
        String operatingSystemType

)implements

Serializable {

}