package egovframework.kt.dxp.api.domain.alarm;

import org.springframework.context.annotation.DependsOn;
import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.C_ALARM;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface AlarmRepository extends JpaDynamicRepository<C_ALARM, String> {

}
