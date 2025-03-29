package egovframework.kt.dxp.api.domain.dong;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_DONG;

@DependsOn("applicationContextHolder")
@Repository
public interface DongRepository extends JpaDynamicRepository<M_DONG, String> {

}
