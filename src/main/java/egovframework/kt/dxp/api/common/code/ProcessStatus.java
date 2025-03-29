package egovframework.kt.dxp.api.common.code;

import lombok.Getter;

@Getter
public enum ProcessStatus {
    ALL("01", "전체"),
    IN_PROGRESS("02", "진행"),
    COMPLETED("03", "종료");

    private final String code;
    private final String description;

    // 생성자
    ProcessStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
