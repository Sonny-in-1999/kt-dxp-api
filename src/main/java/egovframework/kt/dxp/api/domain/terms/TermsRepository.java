package egovframework.kt.dxp.api.domain.terms;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_TRMS;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_TRMS_KEY;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 약관 Repository
 *
 * @author BITNA
 * @apiNote 2024-11-05 BITNA 약관 유형별로 조회가능하도록 변경
 * @since 2024-10-18<br />
 */
@Repository
@DependsOn("applicationContextHolder")
public interface TermsRepository extends JpaDynamicRepository<L_TRMS, L_TRMS_KEY> {

    List<L_TRMS> findByKeyTermsTypeAndKeyTermsStartDateLessThanEqualAndUseYnOrderByKeyTermsStartDateDesc(String termsType, LocalDateTime currentTime, UseYn useYn);

    Optional<L_TRMS> findTopByKeyTermsTypeAndKeyTermsStartDateLessThanEqualAndUseYnOrderByKeyTermsStartDateDesc(String termsType, LocalDateTime currentTime, UseYn useYn);

    Optional<L_TRMS> findByKeyTermsTypeAndKeyTermsStartDateAndUseYn(String termsType,  LocalDateTime termsStartDate, UseYn useYn);
}
