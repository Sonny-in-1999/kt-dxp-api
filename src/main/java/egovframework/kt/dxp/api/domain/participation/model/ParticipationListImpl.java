package egovframework.kt.dxp.api.domain.participation.model;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;

public interface ParticipationListImpl {
        // 항목 명
        String getDivisionName();
        // 항목 코드
        char getDivisionCode();
        // 시퀀스 번호
        Integer getSequenceNumber();
        // 상태
        String getStatus();
        // 사용자 참여 상태 구분
        String getUserStatusDivision();
        // 범주
        String getCategory();
        // 제목
        String getTitle();
        // 결과 및 종료된 여부 Y/N
        UseYn getHasRedBadge();
        // 기간
        String getPeriod();
}