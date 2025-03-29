package egovframework.kt.dxp.api.common.converter;

import static org.apache.commons.lang3.RegExUtils.replaceFirst;

import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.entity.L_FILE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author GEONLEE
 * @apiNote 2024-10-22 BITNA getCurrentLocalDateTime 추가
 * @since 2024-08-08
 */
@Slf4j
public class Converter {

    public static String localDateTimeToFormattedString(LocalDateTime localDateTime) {
        if (ObjectUtils.isEmpty(localDateTime)) {
            return null;
        }
        return localDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
    }

    public static String toUpperCase(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text.toUpperCase();
    }

    /**
     * LocalDateTime을 받아 특정 DateType 형식의 년월일시 문자열로 리턴.
     *
     * @param localDateTime 년월일시
     * @param dateType      포맷
     * @author BokyeongKang
     * @since 2024-07-09<br />
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, DateType dateType) {
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(localDateTime)) {
            return localDateTime.format(
                    DateTimeFormatter.ofPattern(dateType.getPattern(), Locale.KOREA));
        }
        return null;
    }

    /**
     * 년월일시 문자열을 받아 특정 DateType 형식의 LocalDateTime으로 리턴
     *
     * @param dateTimeString 년월일시 문자열
     * @param dateType       포맷
     * @author BokyeongKang
     * @since 2024-07-09<br />
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString, DateType dateType)
            throws DateTimeParseException, IllegalArgumentException {
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(dateTimeString)) {
            return LocalDateTime.parse(dateTimeString,
                    DateTimeFormatter.ofPattern(dateType.getPattern(), Locale.KOREA));
        }
        return null;
    }

    /**
     * 현재 시각 LocalDate로 리턴
     *
     * @author BokyeongKang
     * @since 2024-07-09<br />
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    ///**
    // * 경과시간
    // *
    // * @author MINJI
    // * @apiNote 남은 시간 format 추가
    // * @since 2024-10-23<br />
    // */
    //public static String getElapsedTime(LocalDateTime createDate) {
    //
    //    String nowTime = localDateTimeToString(LocalDateTime.now(), DateType.YYYYMMDDHHMM);
    //    String createTime = localDateTimeToString(createDate, DateType.YYYYMMDDHHMM);
    //
    //    StringBuilder elapseTime = new StringBuilder();
    //
    //    if (nowTime != null && createTime != null) {
    //        Long elTime = Long.parseLong(nowTime) - Long.parseLong(createTime);
    //        // 날짜 계산시 elTime(현재일시 - 특정일시)가 음수인지 양수인지 구분해 뒤에 '남음' 또는 '시간 전'을 붙여서 경과시간을 반환하는 로직
    //        if (elTime > 0) {
    //            elapseTime = new StringBuilder(
    //                    String.valueOf(elTime));
    //            if (elapseTime.length() == 3) {
    //                elapseTime.insert(0, "0");
    //            } else if (elapseTime.length() == 5) {
    //                elapseTime.insert(0, "0");
    //            } else if (elapseTime.length() == 7) {
    //                elapseTime.insert(0, "0");
    //            }
    //
    //            if (elapseTime.length() < 3) {
    //                elapseTime = new StringBuilder(" ");
    //            } else if (elapseTime.length() < 5
    //                    && Long.parseLong(elapseTime.substring(0, 2)) < 24) {
    //                elapseTime = new StringBuilder(
    //                        replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "시간 전");
    //            } else if (elapseTime.length() < 9) {
    //                if (Long.parseLong(elapseTime.substring(0, 2)) >= 7) {
    //                    elapseTime = new StringBuilder(
    //                            createTime.substring(4, 6) + "월 " + createTime.substring(6, 8)
    //                                    + "일");
    //                } else {
    //                    elapseTime = new StringBuilder(
    //                            replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "일 전");
    //                }
    //            }
    //        } else {
    //            elapseTime = new StringBuilder(
    //                    String.valueOf(Math.abs(elTime)));
    //            if (elapseTime.length() == 3) {
    //                elapseTime.insert(0, "0");
    //            } else if (elapseTime.length() == 5) {
    //                elapseTime.insert(0, "0");
    //            } else if (elapseTime.length() == 7) {
    //                elapseTime.insert(0, "0");
    //            }
    //
    //            if (elapseTime.length() < 3) {
    //                elapseTime = new StringBuilder(" ");
    //            } else if (elapseTime.length() < 5
    //                    && Long.parseLong(elapseTime.substring(0, 2)) < 24) {
    //                elapseTime = new StringBuilder(
    //                        replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "시간 남음");
    //            } else if (elapseTime.length() < 9) {
    //                elapseTime = new StringBuilder(
    //                        replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "일 남음");
    //            }
    //        }
    //    }
    //    return elapseTime.toString();
    //}

    /**
     * 현재시간을 format에 맞게 return
     *
     * @author BITNA
     * @since 2024-10-29
     */
    public static String getCurrentDateTimeString(DateType format) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format.getPattern(),
                Locale.KOREA);
        return currentLocalDateTime.format(dateTimeFormatter);
    }

    /**
     * 경과시간
     *
     * @author MINJI
     * @apiNote 기간 지났을 경우 null return 하여 '종료' 알림
     * @since 2024-11-06<br />
     */
    public static StringBuilder getSubtractCurrentTime(LocalDateTime createDate) {
        String nowTime = localDateTimeToString(LocalDateTime.now(), DateType.YYYYMMDDHHMM);
        String createTime = localDateTimeToString(createDate, DateType.YYYYMMDDHHMM);

        StringBuilder elapseTime = new StringBuilder();

        if (nowTime != null && createTime != null) {
            Long elTime = Long.parseLong(nowTime) - Long.parseLong(createTime);
            // 날짜 계산시 elTime(현재일시 - 특정일시)가 음수인지 양수인지 구분해 뒤에 '남음' 또는 '시간 전'을 붙여서 경과시간을 반환하는 로직
            if (elTime > 0) {
                elapseTime = new StringBuilder(
                        String.valueOf(elTime));
                if (elapseTime.length() == 3) {
                    elapseTime.insert(0, "0");
                } else if (elapseTime.length() == 5) {
                    elapseTime.insert(0, "0");
                } else if (elapseTime.length() == 7) {
                    elapseTime.insert(0, "0");
                }

                if (elapseTime.length() < 3) {
                    elapseTime = new StringBuilder(" ");
                } else if (elapseTime.length() < 5
                        && Long.parseLong(elapseTime.substring(0, 2)) < 24) {
                    elapseTime = new StringBuilder(
                            replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "시간 전");
                } else if (elapseTime.length() < 9) {
                    if (Long.parseLong(elapseTime.substring(0, 2)) >= 7) {
                        elapseTime = new StringBuilder(
                                createTime.substring(4, 6) + "월 " + createTime.substring(6, 8)
                                        + "일");
                    } else {
                        new StringBuilder(
                                replaceFirst(elapseTime.substring(0, 2), "^0+", "") + "일 전");
                        elapseTime = null;
                    }
                }
            }
        }
        return elapseTime;
    }


    // 현재 한국 시간 기준으로 경과 시간 계산
    public static String getElapsedTime(LocalDateTime createDate) {
        // 한국 시간대(ZonedDateTime)로 현재 시간을 가져옵니다.
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        // 주어진 시간(createDate)과 현재 시간(now)의 차이를 계산합니다.
        Duration duration = Duration.between(createDate, now);

        // 경과 시간 계산 (일, 시간, 분 단위로 구분)
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        // 경과 시간이 1분 미만일 때 "방금 전"
        if (minutes < 1) {
            return "방금 전";
        }
        // 경과 시간이 1시간 미만일 때 "XX분 전"
        if (days == 0 && hours == 0 && minutes < 60) {
            return minutes + "분 전";
        }
        // 경과 시간이 1시간 이상이고 24시간 미만일 때 "XX시간 전"
        if (days == 0 && hours < 24) {
            return hours + "시간 전";
        }
        // 경과 시간이 24시간 이상일 때 "XX일 전"
        if (days > 0 && days < 7) {
            return days + "일 전";
        }
        // 경과 시간이 1주일 이상일 때 "MM월 dd일" 형식
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");
        return createDate.format(formatter);
    }

    // 기간 계산 및 남은 일 수 반환
    public static String getRemainingDays(LocalDateTime startDate, LocalDateTime endDate) {
        // 현재 날짜
        LocalDateTime currentDate = LocalDateTime.now();

        // 종료일이 지난 경우
        if (currentDate.isAfter(endDate)) {
            return "종료";
        }

        if (!currentDate.isAfter(startDate)) {
            return "오픈 예정";
        }

        // 자정 12시가 지났으면 남은 일수를 계산
        LocalDateTime nextMidnight = startDate.toLocalDate().plusDays(1).atStartOfDay();
        if (!currentDate.isAfter(nextMidnight)) {
            //return "오픈 예정";
        }

        // 종료일까지 남은 일 수 계산
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, endDate);

        return String.format("%d일 남음", daysRemaining);
    }

    // 종료일자 기준으로 자정 12시가 지나면 남은 일수 리턴
    // 하루 남았을 때는 남은 시간과 분 리턴
    // 종료일 지나면 "종료" 리턴
    public static String getRemainingTime(LocalDateTime startDate, LocalDateTime endDate) {
        // 현재 날짜와 시간
        LocalDateTime currentDate = LocalDateTime.now();

        // 종료일이 지난 경우
        if (currentDate.isAfter(endDate)) {
            return "종료";
        }

        if (!currentDate.isAfter(startDate)) {
            return "오픈 예정";
        }

        // 자정 12시가 지났으면 남은 일수를 계산
        LocalDateTime nextMidnight = startDate.toLocalDate().plusDays(1).atStartOfDay();
        if (!currentDate.isAfter(nextMidnight)) {
            //return "오픈 예정";
        }
        // 종료일까지 남은 일 수 계산
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, endDate);

        // 하루 남았을 경우, 남은 시간과 분 계산
        if (daysRemaining == 0) {
            // 종료일까지 남은 시간과 분 계산
            long hoursRemaining = ChronoUnit.HOURS.between(currentDate, endDate) % 24;
            long minutesRemaining = ChronoUnit.MINUTES.between(currentDate, endDate) % 60;

            return (hoursRemaining == 0)
                    ? String.format("%d분 남음", minutesRemaining)
                    : String.format("%d시간 %d분 남음", hoursRemaining, minutesRemaining);
        }

        // 남은 일 수가 1일 이상일 경우
        return String.format("%d일 남음", daysRemaining);
    }

    // file -> base64
    public static String getFileStr(L_FILE entity) {
        String os = System.getProperty("os.name").toLowerCase();
        String path = entity.getFilePath() + "/" + entity.getSaveFileName() + "." + entity.getFileExtension();

        Path saveFilePath = null;
        if (os.contains("linux")) {
            saveFilePath = Path.of(File.separator + path);
        } else if (os.contains("win")) {
            saveFilePath = Path.of(path);
        } else { //linux도 윈도우도 아닐경우. 그냥 상대경로로 저장.
            saveFilePath = Path.of(path);
        }

        byte[] bytes = null;
        try {
            bytes = FileUtils.readFileToByteArray(new File(saveFilePath.toString()));
        } catch (FileNotFoundException e) {
//            LOGGER.error("[N] file not found"); //file을 찾지 못하면 조용히 로그만 남기고 프론트에서 엑박.
        } catch (IOException e) {
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
        }
        String encodBase64 = CommonUtils.encodBase64(bytes);
        return encodBase64;
    }

    public static String getAgeDescription(int startAge, int endAge) {

        // 시작나이나 종료나이가 음수이면 "모든 연령" 반환
        if (Math.signum(startAge) < 0 || Math.signum(endAge) < 0) {
            return "모든 연령";
        }
        return formatAgeRange(startAge, endAge);
    }

    // 나이 범위 포맷팅 함수
    public static String formatAgeRange(int startAge, int endAge) {
        return startAge + "세~" + endAge + "세";
    }

    public static String getRemainingValidDate(LocalDateTime currentDate, LocalDateTime validDate) {
        String statusValue = "Y";

        // 유효일이 지난 경우
        if (currentDate.isAfter(validDate)) {
            statusValue = "N";
            return statusValue;
        }

        return statusValue;
    }

}

