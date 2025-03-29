package egovframework.kt.dxp.api.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilePreviewerImpl {
    boolean supports(MultipartFile file); // 파일 형식을 지원하는지 확인
    boolean supports(String fileName); // 파일 형식을 지원하는지 확인
    String generatePreview(MultipartFile file) throws IOException; // 파일에 대한 미리보기 생성
    String generatePreview(String filePath, String fileName) throws IOException; // 파일 경로와 파일명을 통한 미리보기 생성
    String generateOriginalImage(String filePath, String fileName) throws IOException; // 파일 경로와 파일명을 통한 미리보기 생성
}