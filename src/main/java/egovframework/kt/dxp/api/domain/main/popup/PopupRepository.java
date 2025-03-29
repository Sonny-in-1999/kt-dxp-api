package egovframework.kt.dxp.api.domain.main.popup;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.M_POPUP;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface PopupRepository extends JpaDynamicRepository<M_POPUP, Integer> {

    List<M_POPUP> findByDisplayYnOrderBySortSequenceNumberAsc(UseYn endYn);

    List<M_POPUP> findByDisplayYnAndPopupSequenceNumberNotInOrderBySortSequenceNumberAsc(UseYn endYn, List<Integer> popupSequenceNumber);

    Optional<M_POPUP> findByPopupSequenceNumberAndPopupType(Integer popupSequenceNumber, String popupType);

    //@Query("""
    //        SELECT p
    //          FROM M_POPUP p
    //         WHERE p.popupSequenceNumber = :popupSequenceNumber
    //           AND p.popupType = :popupType
    //        """)
    //Optional<M_POPUP> findByPopupSequenceNumberAndPopupType(
    //        @Param("popupSequenceNumber") Integer popupSequenceNumber,
    //        @Param("popupType") String popupType
    //);

}
