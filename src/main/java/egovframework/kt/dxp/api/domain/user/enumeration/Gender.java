package egovframework.kt.dxp.api.domain.user.enumeration;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
public enum Gender {
    F("0"), M("1");

    private final String genderCode;

    Gender(String genderCode) {
        this.genderCode = genderCode;
    }

    public static Gender getGender(String genderCode) {
        for (Gender gender : Gender.values()) {
            if (gender.genderCode.equals(genderCode)) {
                return gender;
            }
        }
        return null;
    }
}
