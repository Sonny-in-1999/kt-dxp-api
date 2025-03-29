package egovframework.kt.dxp.api.common.code;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
public enum NiceCode implements ResponseCode{

    NICE_ENC_SUCCESS("OK"),
    NICE_ENC_SYSTEM_ERROR("ERR_NICE_ES"),
    NICE_ENC_PROCESS_ERROR("ERR_NICE_EP"),
    NICE_ENC_DATA_ERROR("ERR_NICE_ED"),
    NICE_INPUT_DATA_ERROR("ERR_NICE_ID"),
    NICE_UNKNOWN_ERROR("ERR_NICE_UK"),

    NICE_DEC_SUCCESS("OK"),
    NICE_DEC_SYSTEM_ERROR("ERR_NICE_DS"),
    NICE_DEC_PROCESS_ERROR("ERR_NICE_DP"),
    NICE_DEC_HASH_ERROR("ERR_NICE_HS"),
    NICE_DEC_DATA_ERROR("ERR_NICE_DD"),
    NICE_SITE_PW_ERROR("ERR_NICE_SP");

    private final String code;

    NiceCode(String code) {
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
