package egovframework.kt.dxp.api.common;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GEONLEE
 * @version 1.0.0
 * @Description : 데이터 마스킹 static class
 * mapper 설정에서만 활용하도록 한다.
 * @Modification Information
 * 수정일        수정자               수정내용
 * ----------   ---------   -------------------------------
 * 2023-04-10   GEONLEE      이름 마지막 자리부터 마스킹 처리 EX) 가나다라마 -> 가나***
 * 2023-05-22   GEONLEE      핸드폰번호 마시킹 시 처음4자리 다음4자리 값이 같을 경우 같이 변경되어 앞에만 변경되게 수정 replaceFirst
 * 2023-09-25   MIJIN        nameThird에서 origin == null 일떄 null로 리턴하도록 수정
 * 2024-10-30   BITNA        kt-bo-api에서 가져옴
 * @since 2023-03-06
 * <p>
 * NoCarbon version 1.0
 * <p>
 * Copyright ⓒ 2023 kt corp. All rights reserved.
 * <p>
 * This is a proprietary software of kt corp, and you may not use this file except in
 * compliance with license agreement with kt corp. Any redistribution or use of this
 * software, with or without modification shall be strictly prohibited without prior written
 * approval of kt corp, and the copyright notice above does not evidence any actual or
 * intended publication of such software.
 */
@Slf4j
public class MaskingUtils {
    private static final String REX_KOR = "(^[가-힣]+)$";
    private static final String REX_MOBILE = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
    private static final String REX_LAST_CHAR_MASKING = "^(.*)(.)$";
    private static final String REX_EMAIL = "([\\w.])(?:[\\w.]*)(@.*)";
    private static final String REX_BIRTHDAY = "^((19|20)\\d\\d)?([-/.])?(0[1-9]|1[012])([-/.])?(0[1-9]|[12][0-9]|3[01])$";

    /**
     * 이름 마스킹
     */
    public static String name(String origin) {
        if (origin == null) {
            return "";
        }

        Matcher matcher = Pattern.compile(REX_KOR).matcher(origin);
        if (matcher.find()) {
            int length = origin.length();
            String middleMask = "";
            if (length > 2) {
                middleMask = origin.substring(1, length);
            } else { // 이름이 외자
                middleMask = origin.substring(1, length);
            }
            StringBuilder dot = new StringBuilder();
            dot.append("*".repeat(middleMask.length()));
            if (length > 2) {
                return origin.charAt(0)
                        + middleMask.replace(middleMask, dot.toString());
            }
        }
        return origin;
    }

    /**
     * 이름 1/3 마스킹
     */
    public static String nameThird(String origin) {
        if (origin == null) {
            return null;
        }
        int length = origin.length();
        int maskLength = (int) Math.floor(length / 3.0);
        int startIndex = (length - maskLength) / 2;
        if (length < 3) {
            startIndex = 1;
        } else if (length < 6) {
            startIndex = 2;
        }

        StringBuilder maskedText = new StringBuilder(origin);
        for (int i = startIndex; i < length; i++) {
            maskedText.setCharAt(i, '*');
        }
        return maskedText.toString();
    }

    /**
     * 핸드폰 번호 마스킹
     */
    public static String mobile(String origin) {
        Matcher matcher = Pattern.compile(REX_MOBILE).matcher(origin);
        if (matcher.find()) {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');
            return origin.replaceFirst(target, String.valueOf(c));
        }
        return origin;
    }

    /**
     * 이메일 마스킹
     */
    public static String email(String origin) {
        Matcher matcher = Pattern.compile(REX_EMAIL).matcher(origin);
        if (matcher.find()) {
            String target = matcher.group(2);
            origin = origin.replaceAll("(?<=.{3}).(?=.*@)", "*");
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');
            return origin.replace(target, String.valueOf(c));
        }
        return origin;

    }

    /**
     * 생년월일 마스킹
     */
    public static String birthday(String origin) {
        Matcher matcher = Pattern.compile(REX_BIRTHDAY).matcher(origin);
        if (matcher.find()) {
            return origin.replaceAll("[0-9]", "*");
        }
        return origin;
    }

    // 이름 마스킹 (뒤 1글자만 *로 마스킹)
    public static String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        // 정규표현식을 사용하여 마지막 글자만 *로 변경
        Pattern pattern = Pattern.compile(REX_LAST_CHAR_MASKING);
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches()) {
            String prefix = matcher.group(1);  // 마지막 글자 제외한 부분
            return prefix + "*";  // 마지막 글자는 *로 마스킹
        }

        return name;  // 이름이 1글자일 경우 처리
    }

    // 휴대폰 번호 마스킹 (국번은 3자리 숫자, 010-12**-*678 형식으로)
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }

        Pattern pattern = Pattern.compile(REX_MOBILE);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()) {
            String middle = matcher.group(2);
            String last = matcher.group(3);

            String maskedMiddle = String.format("%s**", middle.substring(0, middle.length()-2));
            String maskedLast   = String.format("*%s", last.substring(1, 4));

            return matcher.group(1) + "-" + maskedMiddle + "-" + maskedLast;
        }
        // 잘못된 형식의 번호는 그대로 반환? -> null 반환?
        return phoneNumber;
    }
}
