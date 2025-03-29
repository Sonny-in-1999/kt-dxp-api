package egovframework.kt.dxp.api.domain.code;

import egovframework.kt.dxp.api.entity.M_CD;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 코드 Repository
 *
 * @author BITNA
 * @since 2024-10-30<br />
 */
@Repository
public interface CodeRepository extends JpaRepository<M_CD, M_CD_KEY> {

    List<M_CD> findByKeyGroupCodeId(String groupCodeId);
    List<M_CD> findByKeyGroupCodeIdAndUseYnOrderBySortSequenceNumberAsc(String groupCodeId, UseYn useYn);

    List<M_CD> findByKeyGroupCodeIdOrderBySortSequenceNumberAsc(String groupCodeId);
}
