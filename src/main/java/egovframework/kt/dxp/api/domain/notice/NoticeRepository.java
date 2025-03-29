package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.domain.notice.model.NoticeListImpl;
import egovframework.kt.dxp.api.entity.M_NOTICE;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 공지사항 Repository
 *
 * @author MINJI
 * @since 2024-10-15
 */
@Repository
@DependsOn("applicationContextHolder")
public interface NoticeRepository extends JpaDynamicRepository<M_NOTICE, Integer> {
    @Query("""
            SELECT masterNotice.noticeSequenceNumber AS noticeSequenceNumber
                 , masterNotice.noticeDivision AS noticeDivision
                 , code.codeName as noticeDivisionName
                 , masterNotice.title AS title
                 , masterNotice.createDate AS createDate
                 , CASE WHEN noticeUsr.key.noticeSequenceNumber IS NULL THEN 'N'
                   ELSE 'Y'
                   END AS alarmCheckYn
                 , CASE WHEN file.bulletinBoardSequenceNumber IS NULL THEN 'N'
                   ELSE 'Y'
                   END AS fileYn
              FROM M_NOTICE masterNotice
         LEFT JOIN L_NOTICE_USR noticeUsr
                ON (masterNotice.noticeSequenceNumber = noticeUsr.key.noticeSequenceNumber
               AND noticeUsr.key.userId = :userId)
         LEFT JOIN L_FILE file
                ON (masterNotice.noticeSequenceNumber = file.bulletinBoardSequenceNumber
               AND file.bulletinBoardDivision = '01')
              JOIN M_CD code
                ON (masterNotice.noticeDivision  = code.key.codeId
               AND code.key.groupCodeId = 'NOTICE_DIV')
             WHERE masterNotice.createDate > :date
          GROUP BY masterNotice.noticeSequenceNumber, masterNotice.noticeDivision, masterNotice.title
          ORDER BY masterNotice.createDate DESC
    """)
    Page<NoticeListImpl> findNoticeListBeforeDate(@Param("userId")String userId, @Param("date") LocalDateTime date, Pageable pageable);

    // 날짜 조건에 맞는 공지사항 조회 (날짜보다 큰 CRT_DT 조건)
    Optional<Page<M_NOTICE>> findByCreateDateAfterOrderByCreateDateDesc(LocalDateTime date, Pageable pageable);


    @Query(nativeQuery = true, value = """
            SELECT CASE WHEN n.NOTICE_SQNO IS NOT NULL THEN 'Y'
                   ELSE 'N' END AS pushMessageYn
              FROM M_NOTICE n
            LEFT JOIN L_NOTICE_USR l
                ON n.NOTICE_SQNO = l.NOTICE_SQNO AND l.USR_ID = :userId
             WHERE l.NOTICE_SQNO IS NULL
            AND n.CRT_DT <= :now and n.CRT_DT > (:now - INTERVAL 1 YEAR)
             LIMIT 1
            """)
    UseYn noticeCheckYn(@Param("userId")String userId, @Param("now")LocalDateTime now);
}
