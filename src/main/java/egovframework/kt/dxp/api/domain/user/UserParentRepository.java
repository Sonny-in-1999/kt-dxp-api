package egovframework.kt.dxp.api.domain.user;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_USR_PRNT;
import java.util.Optional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface UserParentRepository extends JpaDynamicRepository<M_USR_PRNT, String> {
    Optional<M_USR_PRNT> findOneByUserId(String userId);
}