package egovframework.kt.dxp.api.common.file;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class AbstractPdfFilePreviewer implements FilePreviewerImpl {
    private final FileValidator fileValidator = new FileValidator();

    @Override
    public boolean supports(MultipartFile file) {
        return fileValidator.isPdfByMimeType(file);
    }
    @Override
    public boolean supports(String fileName) {
        return fileValidator.isValidPdf(fileValidator.getFileExtension(fileName));
    }
    @Override
    public String generatePreview(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PDDocument document = PDDocument.load(inputStream);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 첫 번째 페이지를 이미지로 변환
            BufferedImage image = pdfRenderer.renderImage(0, 0.5f); // 페이지 크기를 줄여서 렌더링

            // 이미지를 Base64로 인코딩
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    @Override
    public String generatePreview(String filePath, String fileName) throws IOException {
        File pdfFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!pdfFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        try (PDDocument document = PDDocument.load(pdfFile); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 첫 번째 페이지를 이미지로 변환
            BufferedImage image = pdfRenderer.renderImage(0, 0.5f); // 페이지 크기를 50%로 축소

            // 이미지를 Base64로 인코딩하여 반환
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    @Override
    public String generateOriginalImage(String filePath, String fileName) throws IOException {
        File pdfFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!pdfFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        try (PDDocument document = PDDocument.load(pdfFile); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 첫 번째 페이지를 이미지로 변환
            BufferedImage image = pdfRenderer.renderImage(0, 0.5f); // 페이지 크기를 50%로 축소

            // 이미지를 Base64로 인코딩하여 반환
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}
