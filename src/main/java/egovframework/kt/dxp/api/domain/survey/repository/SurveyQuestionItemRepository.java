package egovframework.kt.dxp.api.domain.survey.repository;

import egovframework.kt.dxp.api.entity.L_SURV_QSTN_ITEM;
import egovframework.kt.dxp.api.entity.key.L_SURV_QSTN_ITEM_KEY;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DependsOn("applicationContextHolder")
public interface SurveyQuestionItemRepository extends JpaRepository<L_SURV_QSTN_ITEM, L_SURV_QSTN_ITEM_KEY> {

    List<L_SURV_QSTN_ITEM> findByKeySurveySequenceNumber(Integer surveySequenceNumber);
}
