package egovframework.kt.dxp.api.domain.signUp.record;

import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO for {@link M_USR}
 */
@Builder
@Schema(description = "회원 가입 추가 요청")
public record SignUpTermsRequest(
        ///* 약관 시작 일시      */
        //@Schema(description = "약관 시작 일시 / LocalDate", example = "2024-10-17")
        //@NotNull
        //LocalDate termsStartDate,

        /* 약관 유형           */
        @Schema(description = "약관 유형 / varchar(3)", example = "01")
        @Size(max = 3)
        @NotNull
        String termsType,

        /* 동의 여부           */
        @Schema(description = "동의 여부 / varchar(1)", example = "N")
        @NotNull
        @Enumerated(EnumType.STRING)
        UseYn agreementYn
)implements

Serializable {

}