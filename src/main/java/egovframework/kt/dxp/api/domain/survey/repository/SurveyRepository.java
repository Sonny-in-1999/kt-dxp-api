package egovframework.kt.dxp.api.domain.survey.repository;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_SURV;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("applicationContextHolder")
public interface SurveyRepository extends JpaDynamicRepository<L_SURV, Integer> {

    Integer countBySurveySequenceNumberInAndEndYn(List<Integer> ids, UseYn isCompleted);

    //Batch 에서 통계(종료) 처리를 위해 사용, 종료 일시가 1분 이내이거나 종료여부가 N 인 설문 조회
    List<L_SURV> findByEndDateBetweenAndEndYnOrderByEndDateAsc(LocalDateTime startDate, LocalDateTime endDate, UseYn endYn);

    // 전체 설문
    Page<L_SURV> findByOrderByEndDateAscStartDateAsc(Pageable pageable);

    // 진행중인 설문
    Page<L_SURV> findByEndYnOrderByStartDateAsc(UseYn endYn, Pageable pageable);

    // 종료한 설문
    Page<L_SURV> findByEndYnAndEndDateAfterOrderByEndDateAscStartDateAsc(UseYn endYn, LocalDateTime endDate, Pageable pageable);

    // 참여 가능한 설문
    //Page<L_SURV> findByEndYnAndParticipationStartAgeLessThanEqualAndParticipationEndAgeGreaterThanEqualAndMaleAbleYn(
    //        UseYn endYn, Integer participationStartAge, Integer participationEndAge, UseYn femaleAbleYn, Pageable pageable);
    //Page<L_SURV> findByEndYnAndParticipationStartAgeLessThanEqualAndParticipationEndAgeGreaterThanEqualAndFemaleAbleYn(
    //        UseYn endYn, Integer participationStartAge, Integer participationEndAge, UseYn femaleAbleYn, Pageable pageable);
    //
    //List<L_SURV> findByEndYnAndParticipationStartAgeLessThanEqualAndParticipationEndAgeGreaterThanEqual(
    //        UseYn endYn,
    //        Integer participationStartAge, Integer participationEndAge);
    @Query(value = """
            SELECT ori
              FROM L_SURV ori
             WHERE ori.endYn = :endYn
               AND ori.maleAbleYn = :ableYn
               AND ((ori.participationStartAge <= :userAge AND ori.participationEndAge >= :userAge)
                OR (ori.participationStartAge < 0 AND ori.participationEndAge < 0))
               AND NOT EXISTS (
                      SELECT 'x'
                        FROM L_SURV_USR l_surv_usr
                       WHERE
                            l_surv_usr.key.surveySequenceNumber = ori.surveySequenceNumber
                            AND l_surv_usr.key.userId = :userId
                    )
              """)
    Page<L_SURV> findByMaleParticipationEnabled(UseYn endYn, Integer userAge, UseYn ableYn, String userId, Pageable pageable);

    @Query(value = """
            SELECT ori
              FROM L_SURV ori
             WHERE ori.endYn = :endYn
               AND ori.femaleAbleYn = :ableYn
               AND ((ori.participationStartAge <= :userAge AND ori.participationEndAge >= :userAge)
                OR (ori.participationStartAge < 0 AND ori.participationEndAge < 0))
               AND NOT EXISTS (
                       SELECT 'x'
                        FROM L_SURV_USR l_surv_usr
                       WHERE
                            l_surv_usr.key.surveySequenceNumber = ori.surveySequenceNumber
                            AND l_surv_usr.key.userId = :userId
                    )
              """)
    Page<L_SURV> findByFemaleParticipationEnabled(UseYn endYn, Integer userAge, UseYn ableYn, String userId, Pageable pageable);

    // (전체)참여한 설문
    @Query(nativeQuery = true, value = """
            SELECT ori.*
              FROM L_SURV ori
             WHERE EXISTS (SELECT 'x'
                             FROM L_SURV_USR usr
                            WHERE usr.SURV_SQNO = ori.SURV_SQNO
                              AND usr.USR_ID = :userId
            )
            """)
    Page<L_SURV> totalParticipatedSurvey(@Param("userId") String userId, Pageable pageable);

    // (진행중)참여한 설문
    @Query(nativeQuery = true, value = """
            SELECT ori.*
              FROM L_SURV ori
             WHERE EXISTS (SELECT 'x'
                             FROM L_SURV_USR usr
                            WHERE usr.SURV_SQNO = ori.SURV_SQNO
                              AND usr.USR_ID = :userId
                             AND ori.START_DT <= :startDate AND ori.END_DT >= :endDate
            ) AND END_YN = 'N'
            """ , countQuery = """ 
            SELECT count(*)
              FROM L_SURV ori
             WHERE EXISTS (SELECT 'x'
                             FROM L_SURV_USR usr
                            WHERE usr.SURV_SQNO = ori.SURV_SQNO
                              AND usr.USR_ID = :userId
                             AND ori.START_DT <= :startDate AND ori.END_DT >= :endDate
            ) AND END_YN = 'N'
"""
    )
    Page<L_SURV> inProgressParticipatedSurvey(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable );

    //(종료된)참여한 설문
    @Query(nativeQuery = true, value = """
            SELECT ori.*
              FROM L_SURV ori
             WHERE EXISTS (SELECT 'x'
                             FROM L_SURV_USR usr
                            WHERE usr.SURV_SQNO = ori.SURV_SQNO
                              AND usr.USR_ID = :userId
            )
            AND END_YN = 'Y' AND END_DT < :endDate
            """)
    Page<L_SURV> terminatedProgressParticipatedSurvey(@Param("userId") String userId, @Param("endDate") LocalDateTime endDate, Pageable pageable );

    //미참여 한 설문
    @Query(nativeQuery = true, value = """
            SELECT ori.*
              FROM L_SURV ori
             WHERE NOT EXISTS (SELECT 'x'
                                 FROM L_SURV_USR usr
                                WHERE usr.SURV_SQNO = ori.SURV_SQNO
                                  AND usr.USR_ID = :userId
            )
            """ , countQuery = """ 
            SELECT count(*)
              FROM L_SURV ori
             WHERE NOT EXISTS (SELECT 'x'
                                 FROM L_SURV_USR usr
                                WHERE usr.SURV_SQNO = ori.SURV_SQNO
                                  AND usr.USR_ID = :userId
            )
""")
    Page<L_SURV> notParticipatedSurvey(@Param("userId") String userId, Pageable pageable);

    //(종료된)미참여 한 설문
    @Query(nativeQuery = true, value = """
            SELECT ori.*
              FROM L_SURV ori
             WHERE NOT EXISTS (SELECT 'x'
                                 FROM L_SURV_USR usr
                                WHERE usr.SURV_SQNO = ori.SURV_SQNO
                                  AND usr.USR_ID = :userId
            )
            AND END_YN = 'Y' AND END_DT < :endDate AND END_DT > :endDate - INTERVAL 90 DAY
            """, countQuery = """ 
            SELECT count(*) 
              FROM L_SURV ori
             WHERE NOT EXISTS (SELECT 'x'
                                 FROM L_SURV_USR usr
                                WHERE usr.SURV_SQNO = ori.SURV_SQNO
                                  AND usr.USR_ID = :userId
            )
            AND END_YN = 'Y' AND END_DT < :endDate AND END_DT > :endDate - INTERVAL 90 DAY
"""
    )
    Page<L_SURV> terminatedNotParticipatedSurvey(@Param("userId") String userId, @Param("endDate") LocalDateTime endDate, Pageable pageable);

}
