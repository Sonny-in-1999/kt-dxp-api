package egovframework.kt.dxp.api.domain.survey.repository;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationListImpl;
import egovframework.kt.dxp.api.entity.L_SURV_USR;
import egovframework.kt.dxp.api.entity.key.L_SURV_USR_KEY;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@DependsOn("applicationContextHolder")
public interface SurveyUserRepository extends
                JpaDynamicRepository<L_SURV_USR, L_SURV_USR_KEY> {

        List<L_SURV_USR> findByKeySurveySequenceNumber(Integer surveySequenceNumber);

        Integer countByKeyUserId(String userId);

        @Query(nativeQuery = true, value = """
                        SELECT COUNT(*)
                        FROM (
                        SELECT COUNT(*)  FROM L_SURV_USR
                        WHERE SURV_SQNO = :surveySequenceNumber
                        GROUP BY USR_ID) t1;
                          """)
        Integer getSurveyCount(@Param("surveySequenceNumber") Integer surveySequenceNumber);

        Page<L_SURV_USR> findByKeySurveySequenceNumberAndKeyQuestionSequenceNumberOrderByCreateDateAsc(Integer surveySequenceNumber, Integer questionSequenceNumber, Pageable pageable);

        List<L_SURV_USR> findByKeySurveySequenceNumberAndKeyQuestionSequenceNumberOrderByCreateDateAsc(Integer surveySequenceNumber, Integer questionSequenceNumber);

        List<L_SURV_USR> findByKeyUserIdAndKeySurveySequenceNumber(String userId, Integer surveySequenceNumber);

        List<L_SURV_USR> findByKeyUserId(String userId);

        @Query(nativeQuery = true, value = """
            SELECT '설문 조사' AS divisionName
                  , CAST('S' AS char) AS divisionCode
                  , SURV.SURV_SQNO AS sequenceNumber
                  , CASE WHEN SURV.END_YN = 'Y' THEN '설문 종료'
                    ELSE '설문 진행' END AS status
                  , '03' AS userStatusDivision
                  , '' AS category
                  , SURV.TITLE AS title
                  , SURV.END_YN AS hasRedBadge
                  , CONCAT(DATE_FORMAT(SURV.START_DT, '%Y-%m-%d'), '~', DATE_FORMAT(SURV.END_DT, '%Y-%m-%d')) AS period
                  , MAX(USR.CRT_DT)  AS CRT_DT
              FROM L_SURV SURV
              JOIN L_SURV_USR USR
                ON (SURV.SURV_SQNO = USR.SURV_SQNO
               AND USR.USR_ID  = :userId AND SURV.END_DT > :date)
             WHERE (:isCompleted IS NULL OR SURV.END_YN = :isCompleted)
          GROUP BY SURV.SURV_SQNO, SURV.TITLE, SURV.END_YN, SURV.START_DT, SURV.END_DT
          ORDER BY CRT_DT DESC
    """, countQuery = """
            SELECT COUNT(*)
              FROM (
                     SELECT '설문 조사' AS divisionName
                          , CAST('S' AS char) AS divisionCode
                          , SURV.SURV_SQNO AS sequenceNumber
                          , CASE WHEN SURV.END_YN = 'Y' THEN '설문 종료'
                            ELSE '설문 진행' END AS status
                          , '03' AS userStatusDivision
                          , '' AS category
                          , SURV.TITLE AS title
                          , SURV.END_YN AS hasRedBadge
                          , CONCAT(DATE_FORMAT(SURV.START_DT, '%Y-%m-%d'), '~', DATE_FORMAT(SURV.END_DT, '%Y-%m-%d')) AS period
                          , MAX(USR.CRT_DT)  AS CRT_DT
                      FROM L_SURV SURV
                      JOIN L_SURV_USR USR
                        ON (SURV.SURV_SQNO = USR.SURV_SQNO
                       AND USR.USR_ID  = :userId AND SURV.END_DT > :date)
                     WHERE (:isCompleted IS NULL OR SURV.END_YN = :isCompleted)
                  GROUP BY SURV.SURV_SQNO, SURV.TITLE, SURV.END_YN, SURV.START_DT, SURV.END_DT
                   ) DATA
    """)
        Page<ParticipationListImpl> findParticipationSurveyListBeforeDate(@Param("userId")String userId, @Param("date") LocalDateTime date, @Param("isCompleted") String isCompleted, Pageable pageable);

}
