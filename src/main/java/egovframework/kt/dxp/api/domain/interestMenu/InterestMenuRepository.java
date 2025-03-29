package egovframework.kt.dxp.api.domain.interestMenu;

import egovframework.kt.dxp.api.entity.M_USR_MENU;
import org.springframework.context.annotation.DependsOn;
import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.key.M_USER_MENU_KEY;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.util.List;
import org.springframework.stereotype.Repository;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : InterestMenuRepository
 * Description   : 관심메뉴 Repository
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface InterestMenuRepository extends JpaDynamicRepository<M_USR_MENU, M_USER_MENU_KEY> {

    List<M_USR_MENU> findByKeyUserIdAndMenuUseYnIsOrderBySortSequenceNumberAsc(String userId, UseYn useYn);

    M_USR_MENU findFirstByKeyUserIdOrderBySortSequenceNumberDesc(String userId);

}
