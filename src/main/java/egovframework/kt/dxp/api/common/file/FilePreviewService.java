package egovframework.kt.dxp.api.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilePreviewService {

    private final List<FilePreviewerImpl> previewers = new ArrayList<>();

    public FilePreviewService() {
        previewers.add(new AbstractImageFilePreviewer());
        previewers.add(new AbstractPdfFilePreviewer());
        previewers.add(new AbstractTextFilePreviewer());
    }

    // 파일에 대한 미리보기를 생성
    public String getPreview(MultipartFile file) throws IOException {

        for (FilePreviewerImpl previewer : previewers) {
            if (previewer.supports(file)) {
                return previewer.generatePreview(file);
            }
        }

        throw new UnsupportedOperationException("No preview available for this file type.");
    }

    // 파일에 대한 미리보기를 생성
    public String getPreview(String filePath, String fileName) throws IOException {

        for (FilePreviewerImpl previewer : previewers) {
            if (previewer.supports(fileName)) {
                return previewer.generatePreview(filePath, fileName);
            }
        }

        throw new UnsupportedOperationException("No preview available for this file type.");
    }

    // 파일에 대한 미리보기를 생성
    public String getOriginal(String filePath, String fileName) throws IOException {

        for (FilePreviewerImpl previewer : previewers) {
            if (previewer.supports(fileName)) {
                return previewer.generateOriginalImage(filePath, fileName);
            }
        }

        throw new UnsupportedOperationException("No preview available for this file type.");
    }
}