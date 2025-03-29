package egovframework.kt.dxp.api.common.file;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class AbstractImageFilePreviewer implements FilePreviewerImpl {

    private static final int THUMBNAIL_REGION_WIDTH_PX   = 500;
    private static final int THUMBNAIL_REGION_HEIGHT_PX  = 500;
    private static final int THUMBNAIL_WIDTH_PX  = 150;
    private static final int THUMBNAIL_HEIGHT_PX = 150;
    private final FileValidator fileValidator = new FileValidator();

    @Override
    public boolean supports(MultipartFile file) {
        return fileValidator.isValidImage(file);
    }

    @Override
    public boolean supports(String fileName) {
        return fileValidator.isValidImage(fileValidator.getFileExtension(fileName));
    }

    @Override
    public String generatePreview(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 이미지를 썸네일 크기로 변환
            //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(inputStream)
                    .sourceRegion(Positions.CENTER, THUMBNAIL_REGION_WIDTH_PX, THUMBNAIL_REGION_HEIGHT_PX)
                    .size(THUMBNAIL_WIDTH_PX, THUMBNAIL_HEIGHT_PX) // 썸네일 크기
                    .toOutputStream(outputStream);
            // 썸네일 이미지를 base64로 인코딩
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

    @Override
    public String generatePreview(String filePath, String fileName) throws IOException {
        File imageFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!imageFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }
        // 이미지를 썸네일 크기로 변환
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.of(imageFile)
                    .sourceRegion(Positions.CENTER, THUMBNAIL_REGION_WIDTH_PX, THUMBNAIL_REGION_HEIGHT_PX)
                    .size(THUMBNAIL_WIDTH_PX, THUMBNAIL_HEIGHT_PX) // 썸네일 크기
                    .toOutputStream(outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes); // 이미지를 base64로 인코딩
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

    // byte[] 형태의 이미지 데이터를 받아 썸네일을 생성하고, Base64로 인코딩하여 반환
    public static String generatePreviewFromBytes(byte[] imageBytes) throws IOException {

        if (imageBytes == null) {
            return null;
        }

        // byte[]를 ByteArrayInputStream으로 변환
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 1. 먼저 이미지가 유효한지 확인하기 (ImageIO.read로 이미지 형식 검증)
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            if (bufferedImage == null) {
                return null;
            }
            // 2. 썸네일 크기로 이미지 변환
            byteArrayInputStream.reset(); // 다시 스트림을 처음부터 읽도록 설정
            Thumbnails.of(byteArrayInputStream)  // ByteArrayInputStream으로 이미지 로드
                    .sourceRegion(Positions.CENTER, THUMBNAIL_REGION_WIDTH_PX, THUMBNAIL_REGION_HEIGHT_PX)
                    .size(THUMBNAIL_WIDTH_PX, THUMBNAIL_HEIGHT_PX)            // 썸네일 크기 지정
                    .toOutputStream(outputStream);

            // 썸네일 이미지를 Base64로 인코딩하여 반환
            byte[] thumbnailBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(thumbnailBytes); // Base64로 인코딩된 썸네일 반환
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

    // Base64 인코딩된 이미지 문자열을 받아 썸네일을 생성하고 Base64로 반환
    public static String generatePreviewFromBytes(String imageString) throws IOException {
        if (imageString == null) {
            return null;
        }

        // Base64로 인코딩된 문자열을 byte[]로 디코딩
        byte[] imageBytes = Base64.getDecoder().decode(imageString);

        // byte[]로 받은 이미지 데이터를 사용해 썸네일 생성
        return generatePreviewFromBytes(imageBytes);
    }

    // 원본 이미지를 Base64로 인코딩하여 반환
    public String generateOriginalImageFromBytes(String filePath, String fileName) throws IOException {
        File imageFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!imageFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        String fileExtension = fileValidator.getFileExtension(fileName); // 파일 확장자 확인

        // 원본 이미지를 읽어서 Base64로 변환
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(imageFile); // 이미지 읽기
            if (image == null) {
                throw new IOException("Unable to read image: " + fileName);
            }

            // 이미지 포맷에 맞게 지정 (JPG, PNG, GIF 등)
            boolean success = ImageIO.write(image, fileExtension, outputStream);
            if (!success) {
                throw new IOException("Unsupported image format: " + fileExtension);
            }

            // 원본 이미지를 Base64로 인코딩하여 반환
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes); // Base64로 인코딩된 원본 이미지 반환
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

    // 원본 이미지를 Base64로 인코딩하여 반환
    @Override
    public String generateOriginalImage(String filePath, String fileName) throws IOException {
        File imageFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!imageFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }

        String fileExtension = fileValidator.getFileExtension(fileName); // 파일 확장자 확인

        // 원본 이미지를 읽어서 Base64로 변환
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(imageFile); // 이미지 읽기
            if (image == null) {
                throw new IOException("Unable to read image: " + fileName);
            }

            // 이미지 포맷에 맞게 지정 (JPG, PNG, GIF 등)
            boolean success = ImageIO.write(image, fileExtension, outputStream);
            if (!success) {
                throw new IOException("Unsupported image format: " + fileExtension);
            }

            // 원본 이미지를 Base64로 인코딩하여 반환
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes); // Base64로 인코딩된 원본 이미지 반환
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

    // Base64로 인코딩된 원본 이미지 반환
    public static String generateOriginalImageFromBytes(byte[] imageBytes) {
        if (imageBytes == null) {
            return null;
        }
        // byte[]를 ByteArrayInputStream으로 변환
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 1. 먼저 이미지가 유효한지 확인하기 (ImageIO.read로 이미지 형식 검증)
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            if (bufferedImage == null) {
                return null; // 유효하지 않은 이미지일 경우 null 반환
            }
            byteArrayInputStream.reset(); // 다시 스트림을 처음부터 읽도록 설정

            // ByteArrayOutputStream에 원본 이미지를 그대로 복사
            byte[] imageBytesCopy = byteArrayInputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytesCopy); // Base64로 인코딩된 원본 이미지 반환
        } catch (IOException e) {
            // 예외 발생 시 null을 반환
            return null;
        }
    }

}