package egovframework.kt.dxp.api.domain.participation.model;

import egovframework.kt.dxp.api.entity.enumeration.UseYn;

public interface ParticipationCompletedStatusImpl {
        // 항목 코드
        char getDivisionCode();
        // 결과 및 종료된 여부 Y/N
        UseYn getHasRedBadge();
}