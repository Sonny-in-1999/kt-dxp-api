package egovframework.kt.dxp.api.domain.welfare;

import egovframework.kt.dxp.api.entity.L_WLFR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WelfareRepository extends JpaRepository<L_WLFR, Integer> {
    Page<L_WLFR> findAllByOrderByCreateDateDesc(Pageable pageable);

    Optional<L_WLFR> findByWelfareSequenceNumber(Integer sequenceNumber);

    Optional<Page<L_WLFR>> findByWelfareDivisionOrderByCreateDateDesc(String welfareDivision, Pageable pageable);

    Optional<Page<L_WLFR>> findByWelfareDivisionAndWelfareDetailDivisionOrderByCreateDateDesc(String welfareDivisionCode, String welfareDetailDivisionCode, Pageable pageable);
}
