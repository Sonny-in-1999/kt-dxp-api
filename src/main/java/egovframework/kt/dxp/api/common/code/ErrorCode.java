package egovframework.kt.dxp.api.common.code;

/**
 * @author GEONLEE
 * @since 2024-07-30
 */
public enum ErrorCode implements ResponseCode {
    SERVICE_ERROR("ERR_SV_01"),
    DATA_PROCESSING_ERROR("ERR_SV_02"),
    INVALID_PARAMETER("ERR_CE_01"),
    NO_DATA("ERR_DT_01"),
    EXISTS_DATA("ERR_DT_02"),
    SQL_ERROR("ERR_SQ_01"),
    NOT_AUTHENTICATION("ERR_AT_01"),
    FORBIDDEN("ERR_AT_02"),
    EXPIRED_TOKEN("ERR_AT_03"),
    DUPLICATION_LOGIN("ERR_AT_04");

    private final String code;

    ErrorCode(String code) {
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
