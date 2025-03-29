package egovframework.kt.dxp.api.domain.notice.model;

import java.time.LocalDateTime;

public interface NoticeListImpl {
    /* 공지 순번 */
    Integer getNoticeSequenceNumber();
    /* 공지 구분 */
    String getNoticeDivision();
    /* 공지 구분 명*/
    String getNoticeDivisionName();
    /* 제목 */
    String getTitle();
    /* 생성일시 */
    LocalDateTime getCreateDate();
    /* 읽음 여부 */
    String getAlarmCheckYn();
    /* 첨부파일 여부 */
    String getFileYn();
}