package egovframework.kt.dxp.api.domain.signUp.record;

import egovframework.kt.dxp.api.entity.M_USR;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO for {@link M_USR}
 */
@Builder
@Schema(description = "회원 가입 추가 요청")
public record SignUpRequest(
        /* 본인인증암호화 데이터     */
        @Schema(description = "본인 인증암호화 데이터 / varchar(255)", example = "AgIHTkNDRTY4N2xYT5qIqwnd+cdXdGDvcno7vF36xTjpJxyL9AUNN6YxBOCW6Rv20G0f1YzqionXS3llicNtSJ86o")
        @NotNull
        String myEncryptionData,

        /* 본인인증암호화 데이터     */
        @Schema(description = "부모 인증암호화 데이터 / varchar(255)", example = "AgIHTkNDRTY4N2xYT5qIqwnd+cdXdGDvcno7vF36xTjpJxyL9AUNN6YxBOCW6Rv20G0f1YzqionXS3llicNtSJ86o")
        String parentEncryptionData,

        /* 동 코드                    */
        @Size(max = 5)
        @NotNull
        @Schema(description = "동 코드 / varchar(5)", example = "70000")
        String dongCode,

        /* 자녀 수                    */
        @Schema(description = "자녀 수 / Integer", example = "")
        @NotNull
        Integer childrenCount,

        /* 운영체계 유형              */
        @Size(max = 3)
        @NotNull
        @Schema(description = "운영체계 유형 / varchar(3)", example = "IOS")
        String operatingSystemType,

        /* 푸시 키                    */
        @Size(max = 512)
        @Schema(description = "푸시 키 / varchar(512)", example = "cno7vF36xTjpJxyL")
        String pushKey,

        @NotNull
        SignUpAlarmRequest signUpAlarmData,

        @NotNull
        List<SignUpTermsRequest> signUpTermsList
)implements

Serializable {

}