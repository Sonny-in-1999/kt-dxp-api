package egovframework.kt.dxp.api.domain.survey.repository;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_SURV_QSTN;
import egovframework.kt.dxp.api.entity.key.L_SURV_QSTN_KEY;
import java.util.List;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface SurveyQuestionRepository extends JpaDynamicRepository<L_SURV_QSTN, L_SURV_QSTN_KEY> {

    List<L_SURV_QSTN> findByKeySurveySequenceNumber(Integer surveySequenceNumber);
}
