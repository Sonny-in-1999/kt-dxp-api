package egovframework.kt.dxp.api.common.code;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
public enum LoginCode implements ResponseCode{

    LOGIN_SUCCESS("OK"),
    LOGOUT_SUCCESS("OK"),
    NOT_REGISTERED("LG_NR");

    private final String code;

    LoginCode(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String messageCode() {
        return this.name();
    }
}
