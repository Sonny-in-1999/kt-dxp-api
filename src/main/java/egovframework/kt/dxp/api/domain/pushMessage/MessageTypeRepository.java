package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.code.MSG_TYPE;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;

public interface MessageTypeRepository extends JpaDynamicRepository<MSG_TYPE, M_CD_KEY> {
}
