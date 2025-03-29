package egovframework.kt.dxp.api.domain.interestMenu;

import org.springframework.context.annotation.DependsOn;
import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_MENU;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.util.List;
import org.springframework.stereotype.Repository;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : MenuRepository
 * Description   : 메뉴 Repository
 * Creation Date : 2024-10-22
 * Written by    : MinJi Chae Researcher
 * History       : 1 - 2024-10-22, MinJi Chae, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface MenuRepository extends JpaDynamicRepository<M_MENU, String> {

    List<M_MENU> findByUseYnAndMenuDivisionOrderBySortSequenceNumberAsc(UseYn useYn, String menuDivision);

    M_MENU findByMenuDivisionAndUseYnAndMenuId(String menuDivision, UseYn useYn, String menuId);
}
