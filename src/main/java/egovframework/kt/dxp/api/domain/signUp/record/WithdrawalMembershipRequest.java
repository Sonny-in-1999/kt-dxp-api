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
@Schema(description = "회원 정보 삭제 요청")
public record WithdrawalMembershipRequest(

        /* 사용자 아이디              */
        @Schema(description = "사용자 아이디 / varchar(36)", example = "40232d67-2672-46dd-bf86-131e4371177b")
        @NotNull
        String userId

)implements

Serializable {

}