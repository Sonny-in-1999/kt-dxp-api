package egovframework.kt.dxp.api.common.converter.enumeration;

import lombok.Getter;

/**
 * DateFormat 변경 시 사용하는 ENUM CLASS.
 *
 * @author BokyeeongKang
 * @version 1.0.0
 * @since 2024-07-10
 */
@Getter
public enum DateType {

    /* DATE */
    YYYY("yyyy"),
    YYYYMM("yyyyMM"),
    MMDD("MMdd"),
    YYYYMMDD("yyyyMMdd"),
    /* DATE */
    YYYYMM_FORMAT("yyyy-MM"),
    MMDD_FORMAT("MM-dd"),
    YYYYMMDD_FORMAT("yyyy-MM-dd"),

    /* TIME */
    HHMI("HHmm"),
    HHMISS("HHmmss"),
    HHMISSS("HHmmssSSS"),
    /* TIME */
    HHMI_FORMAT("HH:mm"),
    HHMISS_FORMAT("HH:mm:ss"),
    HHMISSS_FORMAT("HH:mm:ss.SSS"),

    /* DATETIME */
    YYYYMMDDHHMM("yyyyMMddHHmm"),
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    YYYYMMDDHHMMSSS("yyyyMMddHHmmssSSS"),
    /* DATETIME */
    YYYYMMDDHHMM_FORMAT("yyyy-MM-dd HH:mm"),
    YYYYMMDDHHMMSS_FORMAT("yyyy-MM-dd HH:mm:ss"),
    YYYYMMDDHHMMSSS_FORMAT("yyyy-MM-dd HH:mm:ss.SSS");

    private final String pattern;
    DateType(String pattern){
        this.pattern = pattern;
    }

}