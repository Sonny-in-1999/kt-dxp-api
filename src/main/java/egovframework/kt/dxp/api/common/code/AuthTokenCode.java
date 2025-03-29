package egovframework.kt.dxp.api.common.code;

/**
 * @author GEONLEE
 * @since 2024-07-30
 */
public enum AuthTokenCode implements ResponseCode {
    AUTHENTICATION_SUCCESS("OK"),
    REISSUE_TOKEN_SUCCESS("OK");

    private final String code;
    AuthTokenCode(String code) {
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
