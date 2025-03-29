package egovframework.kt.dxp.api.domain.citizen;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.TB_TRX_INFO;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface MobileCitizenTransactionRepository extends JpaDynamicRepository<TB_TRX_INFO, String> {
}