package egovframework.kt.dxp.api.common.code;

import lombok.Getter;

@Getter
public enum ParticipationStatus {
    ALL("01", "전체"),
    AVAILABLE("02", "참여 가능"),
    COMPLETED("03", "참여 완료"),
    NOT_PARTICIPATED("04", "미참여"),
    UNAVAILABLE("05", "불가능");

    private final String code;
    private final String description;

    ParticipationStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
