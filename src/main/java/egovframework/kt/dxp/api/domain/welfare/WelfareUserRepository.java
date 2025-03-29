package egovframework.kt.dxp.api.domain.welfare;

import egovframework.kt.dxp.api.entity.L_WLFR_USR;
import egovframework.kt.dxp.api.entity.key.L_WLFR_USR_KEY;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelfareUserRepository extends JpaRepository<L_WLFR_USR, L_WLFR_USR_KEY> {

    boolean existsAllByKeyWelfareSequenceNumberAndKeyUserId(Integer welfareSequenceNumber, String userId);
}
