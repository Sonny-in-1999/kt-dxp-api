package egovframework.kt.dxp.api.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;


public class FileValidator {

    // 유효한 이미지 파일 확장자 리스트
    // TODO: 아이폰 전용 이미지 확장자 추가
    private static final List<String> VALID_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "svg", "heic", "heif", "tiff", "tif");

    private static final List<String> VALID_PDF_EXTENSIONS = Arrays.asList("pdf");

    private static final List<String> VALID_TEXT_EXTENSIONS = Arrays.asList("txt", "csv", "rtf");

    // 유효한 모든 파일 확장자 리스트 (이미지 외 파일들 포함)
    private static final List<String> VALID_FILE_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "svg", "heic", "heif", "tiff", "tif", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt",
            "csv", "rtf", "hwp", "hwpx", "zip", "tar", "gz", "rar", "mp4", "mkv", "mov", "avi", "wmv", "mp3", "wav", "ogg", "aac");

    // 파일의 확장자가 유효한 파일 확장자 목록에 포함되어 있는지 확인
    public boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 파일 이름에서 확장자 추출
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return false;
        }
        String fileExtension = getFileExtension(filename);

        // 확장자가 유효한 파일 확장자 목록에 있는지 확인
        return VALID_FILE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    // 파일의 확장자가 유효한 파일 확장자 목록에 포함되어 있는지 확인
    public boolean isValidFile(String fileExtension) {
        // 확장자가 유효한 파일 확장자 목록에 있는지 확인
        return VALID_FILE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    // 파일의 확장자가 유효한 파일 확장자 목록에 포함되어 있는지 확인
    public boolean isValidPdf(String fileExtension) {
        // 확장자가 유효한 파일 확장자 목록에 있는지 확인
        return VALID_PDF_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    // 파일의 확장자가 유효한 파일 확장자 목록에 포함되어 있는지 확인
    public boolean isValidText(String fileExtension) {
        // 확장자가 유효한 파일 확장자 목록에 있는지 확인
        return VALID_TEXT_EXTENSIONS.contains(fileExtension.toLowerCase());
    }



    // 파일의 확장자가 유효한 이미지 확장자 목록에 포함되어 있는지 확인
    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 파일 이름에서 확장자 추출
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return false;
        }
        String fileExtension = getFileExtension(filename);

        // 확장자가 유효한 이미지 확장자 목록에 있는지 확인
        return VALID_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase()) && isImageByMimeType(file);
    }

    // 파일의 확장자가 유효한 이미지 확장자 목록에 포함되어 있는지 확인
    public boolean isValidImage(String fileExtension) {
        // 확장자가 유효한 이미지 확장자 목록에 있는지 확인
        return VALID_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    // 확장자 추출 함수 (파일 이름에서 확장자만 가져오는 함수)
    public String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";  // 확장자가 없으면 빈 문자열 반환
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();  // 확장자만 반환 (소문자 변환)
    }

    // MIME 타입으로 이미지 파일인지 확인
    private boolean isImageByMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // MIME 타입으로 PDF 파일인지 확인
    public boolean isPdfByMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("application/pdf");
    }

    // MIME 타입으로 TEXT 파일인지 확인
    public boolean isTextByMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("text/");
    }
}