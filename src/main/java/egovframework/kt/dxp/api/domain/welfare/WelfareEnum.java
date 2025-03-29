package egovframework.kt.dxp.api.domain.welfare;

import lombok.Getter;

@Getter
public class WelfareEnum {

    // WLFR_DIV
    @Getter
    public enum WelfareDivision {
        LIFECYCLE("01"),         // 생애주기별
        DISABILITY("02"),       // 장애인
        LOW_INCOME("03"),      //저소득 지원
        WOMEN_FAMILY("04"),   //여성/가족
        OTHERS("05"),        //기타
        ALL("99");          // 전체 조회

        final String code;
        WelfareDivision(String code) {
            this.code = code;
        }
        public static boolean isValid(String value) {
            for (WelfareDivision division : values()) {
                if (division.getCode().equalsIgnoreCase(value)) {
                    return true;
                }
            }
            return false;
        }
        public static WelfareDivision getEnum(String value) {
            for (WelfareDivision division : values()) {
                if (division.getCode().equalsIgnoreCase(value)) {
                    return division;
                }
            }
            return null;
        }
    }

    @Getter
    public enum WelfareDetailDivision {
        DEFAULT,      // 요청값이 있는 경우
        ALL;         // 전체 조회
    }
}
