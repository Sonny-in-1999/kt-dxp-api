package egovframework.kt.dxp.api.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;


public class AbstractTextFilePreviewer implements FilePreviewerImpl {

    private static final int SHOW_TEXT_LINE = 10;
    private final FileValidator fileValidator = new FileValidator();

    @Override
    public boolean supports(MultipartFile file) {
        return fileValidator.isTextByMimeType(file);
    }
    @Override
    public boolean supports(String fileName) {
        return fileValidator.isValidText(fileValidator.getFileExtension(fileName));
    }
    @Override
    public String generatePreview(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            StringBuilder preview = new StringBuilder();
            String line;
            int lines = 0;

            while ((line = reader.readLine()) != null && lines < SHOW_TEXT_LINE) { // 첫 5줄만 미리보기
                preview.append(line).append("\n");
                lines++;
            }

            return preview.toString();
        }
    }

    @Override
    public String generatePreview(String filePath, String fileName) throws IOException {
        File textFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!textFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            StringBuilder preview = new StringBuilder();
            String line;
            int lines = 0;

            while ((line = reader.readLine()) != null && lines < SHOW_TEXT_LINE) { // 첫 5줄만 미리보기
                preview.append(line).append("\n");
                lines++;
            }

            return preview.toString();
        }
    }

    @Override
    public String generateOriginalImage(String filePath, String fileName) throws IOException {
        File textFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!textFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            StringBuilder preview = new StringBuilder();
            String line;
            int lines = 0;

            while ((line = reader.readLine()) != null && lines < SHOW_TEXT_LINE) { // 첫 5줄만 미리보기
                preview.append(line).append("\n");
                lines++;
            }

            return preview.toString();
        }
    }
}