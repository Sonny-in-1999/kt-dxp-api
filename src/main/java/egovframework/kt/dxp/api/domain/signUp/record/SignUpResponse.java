package egovframework.kt.dxp.api.domain.signUp.record;

import egovframework.kt.dxp.api.entity.C_ALARM;
import egovframework.kt.dxp.api.entity.C_USR_TRMS_AGREE;
import egovframework.kt.dxp.api.entity.M_USR;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO for {@link M_USR}
 */
@Builder
@Schema(description = "회원 가입 추가 응답")
public record SignUpResponse(@Size(max = 36) String userId, @NotNull @Size(max = 50) String userName,
                             @NotNull @Size(max = 512) String password,
                             @Size(max = 30) String mobilePhoneNumber,
                             @Size(max = 3) String genderType,
                             @NotNull @Size(max = 8) String birthDate,
                             @NotNull @Size(max = 5) String dongCode,
                             @NotNull Integer childrenCount,
                             @NotNull @Size(max = 3) String residenceType,
                             @NotNull @Size(max = 512) String certificationId,
                             @NotNull @Size(max = 3) String operatingSystemType,
                             @Size(max = 512) String pushKey, LocalDate recentLoginDateAndTime,
                             LocalDate recentPasswordChangeDateAndTime,
                             @Size(max = 512) String accessToken,
                             @Size(max = 512) String refreshToken,
                             @NotNull LocalDate createDate,
                             @NotNull LocalDate updateDate,
                             C_ALARM cAlarm,
                             Set<C_USR_TRMS_AGREE> cUsrTermsAgrees) implements
        Serializable {

}