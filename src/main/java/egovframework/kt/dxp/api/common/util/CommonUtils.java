package egovframework.kt.dxp.api.common.util;

import egovframework.kt.dxp.api.domain.notice.NoticeRepository;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageRepository;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonUtils {

    private static final String REX_MOBILE = "(\\d{3})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";

    public static String getUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 핸드폰 번호 마스킹
     *
     * @ahthor BITNA
     * @since 2024-10-28
     */
    public static String maskingMobile(String origin) {
        Matcher matcher = Pattern.compile(REX_MOBILE).matcher(origin);
        if (matcher.find()) {
            // 각 그룹을 사용해 필요한 부분만 마스킹
            String masked =
                    matcher.group(1) + "-" + matcher.group(2) + "**-" + matcher.group(4) + "**";
            return masked;
        }
        return origin; // 패턴이 맞지 않으면 원본 반환
    }

    /**
     * 생년월일 formatted
     *
     * @ahthor BITNA
     * @since 2024-10-28
     */
    public static String formatBirthDate(String date) {
        // 문자열 길이가 8인지 확인하여 형식에 맞는지 확인
        if (date.length() == 8) {
            // 연도, 월, 일을 추출하고 `.` 추가하여 포맷팅
            return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
        }
        return date; // 형식이 맞지 않으면 원본 반환
    }

    /**
     * byteImg를 받아 Base64로 이미지로 변환하여 String을 반환
     *
     * @param byteImg byte[]인 이미지
     * @return img string
     * @author jisu
     * @since 2023.06.15
     */
    public static String encodBase64(byte[] byteImg) {
        String img = null;
        if (byteImg != null) {
            img = Base64.encodeBase64String(byteImg);
        }
        return img;
    }

    public static int calculateAge(LocalDateTime birthDate) {
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        // 출생일과 현재 날짜 차이 계산
        Period period = Period.between(birthDate.toLocalDate(), currentDate);
        int age = period.getYears();

        // 생일이 아직 안 지났다면 만나이에서 1을 빼줌
        if (birthDate.toLocalDate().isAfter(currentDate.minusYears(age))) {
            age--;
        }

        return age;
    }

    public static UseYn checkPushMessageYn(String userId,
            PushMessageRepository pushMessageRepository, NoticeRepository noticeRepository) {

        LocalDateTime now = LocalDateTime.now();

        UseYn pushMessage = noticeRepository.noticeCheckYn(userId, now);

        Boolean pushMessageEntity = pushMessageRepository.existsByKeyUserIdAndAlarmCheckYnAndTransmissionDivisionAndKeyCreateDateLessThanEqualAndKeyCreateDateAfter(
                userId,
                UseYn.N,
                TransmissionDivision.Y,
                now, now.minusDays(30));

        if (Boolean.TRUE.equals(pushMessageEntity)) {
            pushMessage = UseYn.Y;
        }

        return pushMessage;
    }

    public static Boolean isUnderAge(String birthDateString, int underAge) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();

            return age < underAge;
        } catch (DateTimeParseException e) {
            log.error("The date format is incorrect.");
            return null;
        }
    }

    public static String combineFileNameAndExtension(String fileName, String fileExtension) {
        if (fileName == null || fileExtension == null) {
            throw new IllegalArgumentException("File name and extension must not be null");
        }

        // 파일 확장자 앞에 점이 없다면 점을 추가합니다.
        if (!fileExtension.startsWith(".")) {
            fileExtension = "." + fileExtension;
        }

        return fileName + fileExtension;
    }

    public static String convertBytes(long bytes) {
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        int unitIndex = 0;
        double value = bytes;

        while (value >= 1024 && unitIndex < units.length - 1) {
            value /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", value, units[unitIndex]);
    }

    public static boolean isAgeInRange(int startAge, int endAge, int userAge) {
        // 음수일 경우 모든 연령을 허용
        if (Math.signum(startAge) < 0 || Math.signum(endAge) < 0) {
            return true;  // 모든 연령을 허용
        }

        // 나이가 지정된 범위 내에 있으면 true
        return startAge <= userAge && userAge <= endAge;
    }

}
