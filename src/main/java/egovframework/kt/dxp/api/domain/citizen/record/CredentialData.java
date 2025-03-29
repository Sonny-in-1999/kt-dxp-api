package egovframework.kt.dxp.api.domain.citizen.record;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.Builder;

@Builder
public record CredentialData(
        String trxCode,
        String expirationDate,
        String issuanceDate,
        String issuerName,
        String issuerCode,
        String approvalTypeCode,
        UseYn isIssued,
        UseYn isChuncheonAddress,
        UseYn bookmarkYn,
        Integer displaySequence,
        String name,
        String address,
        String birth,
        String validDateYn
) {}
