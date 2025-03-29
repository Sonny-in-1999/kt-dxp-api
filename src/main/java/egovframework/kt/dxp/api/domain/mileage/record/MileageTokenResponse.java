package egovframework.kt.dxp.api.domain.mileage.record;

import lombok.Builder;

@Builder
public record MileageTokenResponse(
        String token,
        String cipherkey
) {

}
