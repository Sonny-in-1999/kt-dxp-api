package egovframework.kt.dxp.api.domain.citizen;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_MOBILE_ID;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface MobileCitizenHistoryRepository extends JpaDynamicRepository<L_MOBILE_ID, String> {
    Integer countByKeyUserIdAndKeyTrxCode(String userId, String trxCode);
}