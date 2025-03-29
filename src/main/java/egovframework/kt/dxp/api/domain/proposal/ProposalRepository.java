package egovframework.kt.dxp.api.domain.proposal;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationCompletedStatusImpl;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationListImpl;
import egovframework.kt.dxp.api.entity.C_PRPS;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 정책 제안 Repository
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@Repository
@DependsOn("applicationContextHolder")
public interface ProposalRepository extends JpaDynamicRepository<C_PRPS, Long> {
    @Query(nativeQuery = true, value = """
            SELECT COALESCE(MAX(PRPS_SQNO), 1)
              FROM C_PRPS
            """)
    Long getMaxProposalSequenceNumber();

    @Query(nativeQuery = true, value = """
            SELECT DATA.divisionName as divisionName, DATA.divisionCode as divisionCode, DATA.sequenceNumber as sequenceNumber, DATA.status as status, DATA.userStatusDivision as userStatusDivision, DATA.category as category, DATA.title as title, DATA.hasRedBadge as hasRedBadge, DATA.period as period
              FROM (
                    SELECT '정책 제안' AS divisionName
                          , CAST('P' AS CHAR) AS divisionCode
                          , PRPS.PRPS_SQNO AS sequenceNumber
                          , PRGRS_CODE.CD_NM AS status
                          , '' AS userStatusDivision
                          , PRPS_CODE.CD_NM AS category
                          , PRPS.TITLE AS title
                          , CASE WHEN PRPS.PRPS_PRGRS_DIV = '02' THEN 'Y'
                            ELSE 'N' END AS hasRedBadge
                          , CASE WHEN PRPS.ANS_CRT_DT IS NULL THEN CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'))
                            ELSE CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'), ' · ', '검토일 ', DATE_FORMAT(PRPS.ANS_CRT_DT, '%Y-%m-%d')) END AS period
                          , PRPS.CRT_DT AS CRT_DT
                      FROM C_PRPS PRPS
                      JOIN M_CD PRGRS_CODE
                        ON (PRPS.PRPS_PRGRS_DIV = PRGRS_CODE.CD_ID AND PRGRS_CODE.GRP_CD_ID = 'PRPS_PRGRS_DIV')
                      JOIN M_CD PRPS_CODE
                        ON (PRPS.PRPS_DIV = PRPS_CODE.CD_ID AND PRPS_CODE.GRP_CD_ID = 'PRPS_DIV')
                     WHERE PRPS.CRTUSR_ID = :userId
                       AND (PRPS.ANS_CRT_DT IS NULL OR PRPS.ANS_CRT_DT > :date)
                    UNION ALL
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
                  GROUP BY VOTE.VOTE_SQNO, VOTE.TITLE, VOTE.END_YN, VOTE.START_DT, VOTE.END_DT 
                    UNION ALL
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
                  GROUP BY SURV.SURV_SQNO, SURV.TITLE, SURV.END_YN, SURV.START_DT, SURV.END_DT
                       ) DATA
                    ORDER BY DATA.CRT_DT DESC
    """, countQuery = """
            SELECT COUNT(*)
              FROM (
                    SELECT '정책 제안' AS divisionName
                          , CAST('P' AS CHAR) AS divisionCode
                          , PRPS.PRPS_SQNO AS sequenceNumber
                          , PRGRS_CODE.CD_NM AS status
                          , '' AS userStatusDivision
                          , PRPS_CODE.CD_NM AS category
                          , PRPS.TITLE AS title
                          , CASE WHEN PRPS.PRPS_PRGRS_DIV = '02' THEN 'Y'
                            ELSE 'N' END AS hasRedBadge
                          , CASE WHEN PRPS.ANS_CRT_DT IS NULL THEN CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'))
                            ELSE CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'), ' · ', '검토일 ', DATE_FORMAT(PRPS.ANS_CRT_DT, '%Y-%m-%d')) END AS period
                          , PRPS.CRT_DT AS CRT_DT
                      FROM C_PRPS PRPS
                      JOIN M_CD PRGRS_CODE
                        ON (PRPS.PRPS_PRGRS_DIV = PRGRS_CODE.CD_ID AND PRGRS_CODE.GRP_CD_ID = 'PRPS_PRGRS_DIV')
                      JOIN M_CD PRPS_CODE
                        ON (PRPS.PRPS_DIV = PRPS_CODE.CD_ID AND PRPS_CODE.GRP_CD_ID = 'PRPS_DIV')
                     WHERE PRPS.CRTUSR_ID = :userId
                       AND (PRPS.ANS_CRT_DT IS NULL OR PRPS.ANS_CRT_DT > :date)
                    UNION ALL
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
                  GROUP BY VOTE.VOTE_SQNO, VOTE.TITLE, VOTE.END_YN, VOTE.START_DT, VOTE.END_DT 
                    UNION ALL
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
                  GROUP BY SURV.SURV_SQNO, SURV.TITLE, SURV.END_YN, SURV.START_DT, SURV.END_DT
                       ) DATA
    """)
    Page<ParticipationListImpl> findAllParticipationListBeforeDate(@Param("userId")String userId, @Param("date") LocalDateTime date, Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT '정책 제안' AS divisionName
              , CAST('P' AS CHAR) AS divisionCode
              , PRPS.PRPS_SQNO AS sequenceNumber
              , PRGRS_CODE.CD_NM AS status
              , '03' AS userStatusDivision
              , PRPS_CODE.CD_NM AS category
              , PRPS.TITLE AS title
              , CASE WHEN PRPS.PRPS_PRGRS_DIV = '02' THEN 'Y'
                ELSE 'N' END AS hasRedBadge
              , CASE WHEN PRPS.ANS_CRT_DT IS NULL THEN CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'))
                ELSE CONCAT('제안일 ', DATE_FORMAT(PRPS.CRT_DT, '%Y-%m-%d'), ' · ', '검토일 ', DATE_FORMAT(PRPS.ANS_CRT_DT, '%Y-%m-%d')) END AS period
              , PRPS.CRT_DT AS CRT_DT
          FROM C_PRPS PRPS
          JOIN M_CD PRGRS_CODE
            ON (PRPS.PRPS_PRGRS_DIV = PRGRS_CODE.CD_ID AND PRGRS_CODE.GRP_CD_ID = 'PRPS_PRGRS_DIV')
          JOIN M_CD PRPS_CODE
            ON (PRPS.PRPS_DIV = PRPS_CODE.CD_ID AND PRPS_CODE.GRP_CD_ID = 'PRPS_DIV')
         WHERE PRPS.CRTUSR_ID = :userId
           AND (PRPS.ANS_CRT_DT IS NULL OR PRPS.ANS_CRT_DT > :date)
           AND (:checkDivListNull IS NULL OR PRPS.PRPS_PRGRS_DIV IN (:prgrsDivList))
      ORDER BY PRPS.CRT_DT DESC
    """)
    Page<ParticipationListImpl> findParticipationProposalListBeforeDate(@Param("userId")String userId, @Param("date") LocalDateTime date, @Param("checkDivListNull") String checkDivListNull, @Param("prgrsDivList") List<String> prgrsDivList, Pageable pageable);

    @Query(nativeQuery = true, value = """
                    SELECT CAST('P' AS CHAR) AS divisionCode
                          ,CASE WHEN PRPS.PRPS_PRGRS_DIV = '02' THEN 'Y'
                            ELSE 'N' END AS hasRedBadge
                      FROM C_PRPS PRPS
                     WHERE PRPS.CRTUSR_ID = :userId
                       AND (PRPS.ANS_CRT_DT IS NULL OR PRPS.ANS_CRT_DT > :date)
                       AND PRPS_PRGRS_DIV = '02'
                     UNION
                    SELECT CAST('V' AS char) AS divisionCode
                          ,VOTE.END_YN AS hasRedBadge
                      FROM L_VOTE VOTE
                      JOIN L_VOTE_USR USR
                        ON (VOTE.VOTE_SQNO = USR.VOTE_SQNO
                       AND USR.USR_ID  = :userId AND VOTE.END_DT > :date)
                     WHERE VOTE.END_YN = 'Y'
                     UNION
                    SELECT CAST('S' AS char) AS divisionCode
                          ,SURV.END_YN AS hasRedBadge
                      FROM L_SURV SURV
                      JOIN L_SURV_USR USR
                        ON (SURV.SURV_SQNO = USR.SURV_SQNO
                       AND USR.USR_ID  = :userId AND SURV.END_DT > :date)
                     WHERE SURV.END_YN = 'Y'
    """)
    List<ParticipationCompletedStatusImpl> findServiceResultAndCompletedStatus(@Param("userId")String userId, @Param("date") LocalDateTime date);

    // 사용자의 정책 총 건수
    Integer countByCreateUserId(String userId);
    // 사용자의 정책 결과 건수
    Integer countByCreateUserIdAndProposalProgressDivisionCodeKeyCodeId(String userId, String codeId);

}
