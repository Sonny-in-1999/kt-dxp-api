package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.jpa.JpaDynamicRepository;
import egovframework.kt.dxp.api.entity.L_NOTICE_USR;
import egovframework.kt.dxp.api.entity.key.L_NOTICE_USR_KEY;
import java.util.List;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

/**
 * 이력 공지 사용자 Repository
 *
 * @author MINJI
 * @apiNote 2024-10-29 BITNA notice alarmCheck 컬럼 조회를 위한 쿼리 메소드 추가
 * @since 2024-10-16
 */
@Repository
@DependsOn("applicationContextHolder")
public interface NoticeUserRepository extends JpaDynamicRepository<L_NOTICE_USR, L_NOTICE_USR_KEY> {

    List<L_NOTICE_USR> findByKeyUserId(String userId);

    Boolean existsByKeyUserIdAndKeyNoticeSequenceNumber(String userId, Integer noticeSequenceNumber);
}
