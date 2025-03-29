package egovframework.kt.dxp.api.domain.participation;

import lombok.Getter;

@Getter
public class ParticipationEnum {

    @Getter
    public enum DivisionCode {
        PROPOSAL('P'),
        VOTE('V'),
        SURVEY('S');

        private final char value;

        DivisionCode(char value) {
            this.value = value;
        }

    }

    public enum SearchDivision {
        ALL,         // 전체 조회
        PROPOSAL,    // 제안
        VOTE,        // 투표
        SURVEY;      // 설문

        // enum에서 String 값이 유효한지 확인하는 메서드 추가
        public static boolean isValid(String value) {
            for (SearchDivision division : SearchDivision.values()) {
                if (division.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum SearchSubDivision {
        ALL,         // 전체 서브 구분
        PROGRESS,    // 진행 중
        COMPLETE;    // 완료

        // enum에서 String 값이 유효한지 확인하는 메서드 추가
        public static boolean isValid(String value) {
            for (SearchSubDivision division : SearchSubDivision.values()) {
                if (division.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
