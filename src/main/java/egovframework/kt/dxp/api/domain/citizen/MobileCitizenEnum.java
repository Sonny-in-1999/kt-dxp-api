package egovframework.kt.dxp.api.domain.citizen;

import java.util.List;
import lombok.Getter;

public class MobileCitizenEnum {
    @Getter
    public enum ServiceEndpoint {
        //	/** 결과 */
        //	private Boolean result;
        //	/** 데이터 */ BASE 64로 변환해서 전달
        //	private String data;

        // 공통 REQUEST 인지 여부를 판단해서 전송하자!
        // 요청 URL은 하나로 고정해서

        QRMPM_START  ("/qrmpm/start"        , true, "510", "QRMPM 시작"),                // mode : 모드(direct/proxy)
        QRCPM_START  ("/qrcpm/start"        , true, "520", "QRCPM 시작"),                // mode : 모드(direct/proxy)
        APP2APP_START("/app2app/start"      , true, "530", "App to App 시작"),           // mode : 모드(direct/indirect)
        PUSH_START   ("/push/start"         , true, "540", "PUSH 시작"),                 // mode : 모드(direct/proxy)
        MIP_PROFILE  ("/mip/profile"        , true, "310", "Profile 요청"),
        MIP_IMAGE    ("/mip/image"          , true, "320", "Image 요청"),
        MIP_VP       ("/mip/vp"             , true, "400", "VP 검증"),                   // request : presentation로 고정
        MIP_ERROR    ("/mip/error"          , true, "900", "오류 전송"),
        MIP_TRXSTS   ("/mip/trxsts"         , true, ""   , "거래상태 조회"),              // request : trxcode(거래코드)
        MIP_REVP     ("/mip/revp"           , true, ""   , "VP 재검증"),
        MIP_VPDATA   ("/mip/vpdata"         , true, ""   , "VP Data 조회");

        private final String  url;
        private final Boolean isCommonRequest;
        private final String  command;
        private final String  description;

        ServiceEndpoint(String url, Boolean isCommonRequest, String command, String description) {
            this.url = url;
            this.isCommonRequest = isCommonRequest;
            this.command = command;
            this.description = description;
        }
    }

    public enum Status {
        REQUESTED,     // 신청
        APPROVED,     // 승인
        REJECTED,    // 반려
        EXPIRED,    // 만료
        REVOKED    // 폐기
    }

    public enum PrivacyType {
        ADDRESS("address"),
        NAME("name"),
        BIRTH("birth"),
        UNKNOWN("");

        private final String value;

        PrivacyType(String value) {
            this.value = value;
        }

        public static PrivacyType fromString(String text) {
            if (text != null) {
                for (PrivacyType type : PrivacyType.values()) {
                    if (type.value.equalsIgnoreCase(text)) {
                        return type;
                    }
                }
            }
            return UNKNOWN;
        }
    }

    public enum IdentityCode {
        MUNICIPAL_ID("M01", "춘천 디지털 신분증"),
        PREGNANCY_ID("P01", "임산부증"),
        LIBRARY_ID("L01", "춘천 도서관 출입증"),
        CULTURE_ID("C01", "문화센터 출입증");

        private final String code;
        private final String description;

        IdentityCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static IdentityCode fromCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                return MUNICIPAL_ID; // 기본값으로 MUNICIPAL_ID 반환
            }

            for (IdentityCode type : IdentityCode.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return MUNICIPAL_ID; // 일치하는 코드가 없을 경우에도 MUNICIPAL_ID 반환
        }

        // issuerCode에 해당하는 IdentityCode를 찾는 메소드
        public static IdentityCode findCode(String code) {
            for (IdentityCode identityCode : IdentityCode.values()) {
                if (identityCode.getCode().equals(code)) {
                    return identityCode;
                }
            }
            throw new IllegalArgumentException("Unknown issuer code: " + code);
        }
        // 모든 IdentityCode를 List로 반환
        public static List<IdentityCode> getAllIdentityCodes() {
            return List.of(IdentityCode.values());
        }

        public boolean isMunicipalId() {
            return this == MUNICIPAL_ID;
        }
    }

    public enum IssuerType {
        DRIVERS_LICENSE("DL01", "운전면허증", "운전면허"),
        RESIDENT_CARD("ID01", "주민증", "주민"),
        VETERANS_CARD("VA01", "국가보훈등록증", "보훈"),
        OVERSEAS_KOREAN("OE01", "재외국민 신원 확인증", "재외국민"),
        UNKNOWN("UK01", "알 수 없음", "");  // UNKNOWN 코드 추가

        private final String code;
        private final String name;
        private final String keyword;

        IssuerType(String code, String name, String keyword) {
            this.code = code;
            this.name = name;
            this.keyword = keyword;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getKeyword() {
            return keyword;
        }

        /**
         * 발급자 이름으로 IssuerType 찾기
         */
        public static IssuerType fromIssuerName(String issuerName) {
            if (issuerName == null || issuerName.trim().isEmpty()) {
                return UNKNOWN;
            }

            for (IssuerType type : IssuerType.values()) {
                if (type != UNKNOWN && issuerName.contains(type.keyword)) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        /**
         * 코드로 IssuerType 찾기
         */
        public static IssuerType fromCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                return UNKNOWN;
            }

            for (IssuerType type : IssuerType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        /**
         * 알려진 발급자인지 확인 (UNKNOWN이 아닌지)
         */
        public boolean isKnownIssuer() {
            return this != UNKNOWN;
        }

        /**
         * 코드가 유효한지 확인 (알려진 코드인지)
         */
        public static boolean isValidCode(String code) {
            return fromCode(code) != UNKNOWN;
        }

        /**
         * 코드가 UNKNOWN인지 확인
         */
        public boolean isUnknown() {
            return this == UNKNOWN;
        }
    }
}
