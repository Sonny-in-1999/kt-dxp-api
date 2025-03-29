package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_PUSH_MSG;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_PUSH_MSG_KEY;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   : 알림 조회 Repository
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@Repository
@DependsOn("applicationContextHolder")
public interface PushMessageRepository extends JpaDynamicRepository<L_PUSH_MSG, L_PUSH_MSG_KEY> {

    L_PUSH_MSG findByLink(String link);
    Optional<L_PUSH_MSG> findByKey_UserIdAndKey_CreateDateAndKey_MessageTypeAndLink(String userId, LocalDateTime date, String messageType, String link);

    @Query("""
                SELECT a 
                  FROM L_PUSH_MSG a 
                 WHERE a.key.userId = :userId 
                   AND a.key.createDate > :date 
                   AND a.transmissionDivision = 'Y'
              ORDER BY a.transmissionRequestDate desc
           """)
    Optional<Page<L_PUSH_MSG>> findMyNotificationBeforeDate(String userId, @Param("date") LocalDateTime date, Pageable pageable);
    // NOTE: 위 아래 동일한 기능 Sample로 남겨둠
    // Optional<Page<L_PUSH_MSG>> findByKey_UserIdAndKey_CreateDateGreaterThanAndTransmissionYnOrderByTransmissionRequestDateDesc(String userId, LocalDateTime date, TransmissionDivision transmissionDivision, Pageable pageable);

    @Query("""
                    select pm
                      from L_PUSH_MSG pm
                     where pm.transmissionRequestDate between :oneMinuteBefore and :now
                       and pm.transmissionDivision = :transmissionDivision
                     order by pm.transmissionRequestDate desc
            """)
    List<L_PUSH_MSG> getUndeliveredMessages(
            @Param("now") LocalDateTime now,
            @Param("oneMinuteBefore") LocalDateTime oneMinuteBefore,
            @Param("transmissionDivision") TransmissionDivision transmissionDivision
    );

    Boolean existsByKeyUserIdAndAlarmCheckYnAndTransmissionDivisionAndKeyCreateDateLessThanEqualAndKeyCreateDateAfter(String userId, UseYn alarmYn, TransmissionDivision transmissionDivision, LocalDateTime startDate, LocalDateTime endDate);

}
