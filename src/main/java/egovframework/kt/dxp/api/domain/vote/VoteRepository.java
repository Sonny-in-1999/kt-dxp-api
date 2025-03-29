package egovframework.kt.dxp.api.domain.vote;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationListImpl;
import egovframework.kt.dxp.api.entity.L_VOTE;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-10-30
 */
@Repository
@DependsOn("applicationContextHolder")
public interface VoteRepository extends JpaDynamicRepository<L_VOTE, Integer> {

    Integer countByVoteSequenceNumberInAndEndYn(List<Integer> ids, UseYn isCompleted);

    List<L_VOTE> findByEndDateBetweenAndEndYnOrderByEndDateAsc(LocalDateTime startDate, LocalDateTime endDate, UseYn endYn);

    List<L_VOTE> findByStartDateLessThanEqualAndEndDateAfterOrderByEndYnAscEndDateAscStartDateAsc(LocalDateTime curDateTime, LocalDateTime before30Date);
    @Query(nativeQuery = true, value = """
        SELECT '투표 참여' AS divisionName
              , CAST('V' AS char) AS divisionCode
              , VOTE.VOTE_SQNO AS sequenceNumber
              , CASE WHEN VOTE.END_YN = 'Y' THEN '투표 종료'
                ELSE '투표 진행' END AS status
              , '03' AS userStatusDivision
              , '' AS category
              , VOTE.TITLE AS title
              , VOTE.END_YN AS hasRedBadge
              , CONCAT(DATE_FORMAT(VOTE.START_DT, '%Y-%m-%d'), '~', DATE_FORMAT(VOTE.END_DT, '%Y-%m-%d')) AS period
              , MAX(USR.CRT_DT)  AS CRT_DT
          FROM L_VOTE VOTE
          JOIN L_VOTE_USR USR
            ON (VOTE.VOTE_SQNO = USR.VOTE_SQNO
           AND USR.USR_ID  = :userId AND VOTE.END_DT > :date)
         WHERE (:isCompleted IS NULL OR VOTE.END_YN = :isCompleted)
      GROUP BY VOTE.VOTE_SQNO, VOTE.TITLE, VOTE.END_YN, VOTE.START_DT, VOTE.END_DT 
      ORDER BY CRT_DT DESC
    """, countQuery = """
            SELECT COUNT(*)
              FROM (
                    SELECT '투표 참여' AS divisionName
                          , CAST('V' AS char) AS divisionCode
                          , VOTE.VOTE_SQNO AS sequenceNumber
                          , CASE WHEN VOTE.END_YN = 'Y' THEN '투표 종료'
                            ELSE '투표 진행' END AS status
                          , '03' AS userStatusDivision
                          , '' AS category
                          , VOTE.TITLE AS title
                          , VOTE.END_YN AS hasRedBadge
                          , CONCAT(DATE_FORMAT(VOTE.START_DT, '%Y-%m-%d'), '~', DATE_FORMAT(VOTE.END_DT, '%Y-%m-%d')) AS period
                          , MAX(USR.CRT_DT)  AS CRT_DT
                      FROM L_VOTE VOTE
                      JOIN L_VOTE_USR USR
                        ON (VOTE.VOTE_SQNO = USR.VOTE_SQNO
                       AND USR.USR_ID  = :userId AND VOTE.END_DT > :date)
                     WHERE (:isCompleted IS NULL OR VOTE.END_YN = :isCompleted)
                  GROUP BY VOTE.VOTE_SQNO, VOTE.TITLE, VOTE.END_YN, VOTE.START_DT, VOTE.END_DT 
                   ) DATA
    """)
    Page<ParticipationListImpl> findParticipationVoteListBeforeDate(@Param("userId")String userId, @Param("date") LocalDateTime date, @Param("isCompleted") String isCompleted, Pageable pageable);

}
