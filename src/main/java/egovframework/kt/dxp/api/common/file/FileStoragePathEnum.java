package egovframework.kt.dxp.api.common.file;

import egovframework.kt.dxp.api.common.CommonVariables;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public enum FileStoragePathEnum {
    NOTICE,
    PROPOSAL,
    SURVEY,
    POPUP,
    VOTE,
    WELFARE,
    BANNER,
    DESTINATION,
    THUMBNAIL,
    DEFAULT;

    private static final String FILE_SAVE_DIR = "/fileupload/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String BASE_PATH = CommonVariables.FILE_BASE_PATH;

//    @Value("${file.base-path:/data/}")

//    // 외부에서 basePath 값을 주입할 수 있도록 설정
//    public void setBasePath(String basePath) {
//        this.basePath = basePath;
//    }

    // 오늘 날짜를 'YYYYMMDD' 형식으로 반환
    public String getFormattedDate() {
        return LocalDate.now().format(formatter);
    }

    // 기본 폴더 경로 반환
    public String getStoragePath(String destinationDir) {
        String dateStr = getFormattedDate();
        return switch(this) {
            case NOTICE ->
                // 공지사항 처리
                    BASE_PATH + FILE_SAVE_DIR + NOTICE.name().toLowerCase() + "/" + dateStr;
            case PROPOSAL ->
                // 정책 처리
                    BASE_PATH + FILE_SAVE_DIR + PROPOSAL.name().toLowerCase() + "/" + dateStr;
            case SURVEY ->
                // 설문 처리
                    BASE_PATH + FILE_SAVE_DIR + SURVEY.name().toLowerCase() + "/" + dateStr;
            case POPUP ->
                // 팝업 처리
                    BASE_PATH + FILE_SAVE_DIR + POPUP.name().toLowerCase() + "/" + dateStr;
            case VOTE ->
                // 투표 처리
                    BASE_PATH + FILE_SAVE_DIR + VOTE.name().toLowerCase() + "/" + dateStr;
            case WELFARE ->
                // 복지 처리
                    BASE_PATH + FILE_SAVE_DIR + WELFARE.name().toLowerCase() + "/" + dateStr;
            case BANNER ->
                // 배너 처리
                    BASE_PATH + FILE_SAVE_DIR + BANNER.name().toLowerCase() + "/" + dateStr;
            case THUMBNAIL ->
                // 썸네일 처리
                    BASE_PATH + FILE_SAVE_DIR + THUMBNAIL.name().toLowerCase() + "/" + dateStr;
            case DESTINATION ->
                // 지정된 폴더가 있는 경우
                    BASE_PATH + FILE_SAVE_DIR + destinationDir + "/" + dateStr;
            default -> BASE_PATH + FILE_SAVE_DIR + dateStr;
        };
    }
}