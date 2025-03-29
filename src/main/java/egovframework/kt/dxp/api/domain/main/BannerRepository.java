package egovframework.kt.dxp.api.domain.main;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_BANNER;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface BannerRepository extends JpaDynamicRepository<M_BANNER, Integer> {

    List<M_BANNER> findTop9ByDisplayYnOrderBySortSequenceNumberAsc(UseYn useYn);
}
