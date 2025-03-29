package egovframework.kt.dxp.api.common.file;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public abstract class AbstractFileHandler implements FileHandlerImpl {
    private static final int THUMBNAIL_REGION_WIDTH_PX   = 500;
    private static final int THUMBNAIL_REGION_HEIGHT_PX  = 500;
    private static final int THUMBNAIL_WIDTH_PX  = 150;
    private static final int THUMBNAIL_HEIGHT_PX = 150;
    private final FileValidator fileValidator = new FileValidator();

    @Override
    public void uploadFile(MultipartFile file, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException {
        // 업로드 가능한 파일인지 확인
        if (!fileValidator.isValidFile(file)) {
            throw new IllegalArgumentException("Files with extensions that can't be uploaded.");
        }

        Path path = Paths.get(fileStorageType.getStoragePath(destinationDir), file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        log.info("File uploaded to: {}", path);
    }

    @Override
    public void uploadFileWithThumbnail(MultipartFile file, String fileName, FileStoragePathEnum originalPath, FileStoragePathEnum thumbnailPath, String destinationDir) throws IOException {
        // 업로드 가능한 파일인지 확인
        if (!fileValidator.isValidFile(file)) {
            throw new IllegalArgumentException("Files with extensions that can't be uploaded.");
        }
        String fileExtension = fileValidator.getFileExtension(file.getOriginalFilename());
        saveThumbnailWithFileName(file, thumbnailPath, fileName);

        Path path = Paths.get(originalPath.getStoragePath(destinationDir),fileName + "." + fileExtension);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        log.info("File uploaded to: {}", path);
    }

    @Override
    public void uploadFile(List<MultipartFile> files, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException {
        for (MultipartFile file : files) {
            uploadFile(file, fileStorageType, destinationDir);
        }
    }

    @Override
    public void uploadFileWithFileName(MultipartFile file, String fileName, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException {
        // 업로드 가능한 파일인지 확인
        if (!fileValidator.isValidFile(file)) {
            throw new IllegalArgumentException("Files with extensions that can't be uploaded.");
        }
        // 파일 이름에서 확장자 추출
        String fileExtension = fileValidator.getFileExtension(file.getOriginalFilename());

        Path path = Paths.get(fileStorageType.getStoragePath(destinationDir),fileName + "." + fileExtension);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        log.info("File uploaded to: {}", path);
    }

    @Override
    public void uploadFileWithFileName(List<MultipartFile> files, List<String> fileNames, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException {
        for (MultipartFile file : files) {
            for (String fileName : fileNames) {
                uploadFileWithFileName(file, fileName, fileStorageType, destinationDir);
            }
        }
    }

    @Override
    public Resource downloadResourceFile(String fileName, String destinationDir) throws IOException {
        Path filePath = Paths.get(destinationDir, fileName);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return new UrlResource(file.toURI());
    }

    @Override
    public Resource downloadResourceFile(String fullPathWithFileName) throws IOException {
        Path filePath = Paths.get(fullPathWithFileName);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fullPathWithFileName);
        }
        return new UrlResource(file.toURI());
    }

    @Override
    public InputStream downloadInputStreamFile(String fullPathWithFileName) throws IOException {
        Path filePath = Paths.get(fullPathWithFileName);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fullPathWithFileName);
        }
        return new FileInputStream(file);
    }

    @Override
    public InputStream downloadInputStreamFile(String fileName, String destinationDir) throws IOException {
        Path filePath = Paths.get(destinationDir, fileName);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return new FileInputStream(file);
    }

    @Override
    public BufferedImage generateThumbnail(MultipartFile file) throws IOException {
        // 파일이 이미지인지 확인
        if (!fileValidator.isValidImage(file)) {
            throw new IllegalArgumentException("File is not an image, cannot generate thumbnail.");
        }
        // Thumbnailator를 이용해 썸네일 생성
        try(InputStream inputStream = file.getInputStream()){
            return Thumbnails.of(inputStream)
//                    .sourceRegion(Positions.CENTER, THUMBNAIL_REGION_WIDTH_PX, THUMBNAIL_REGION_HEIGHT_PX)
                    .size(THUMBNAIL_WIDTH_PX, THUMBNAIL_HEIGHT_PX)  // 썸네일 크기 지정
                    .asBufferedImage(); // BufferedImage로 반환
        }
    }

    // NOTE: 기존에 저장된 파일을 Thumbnail로 생성하는 기능이 필요할까?
    @Override
    public BufferedImage generateThumbnail(String filePath, String fileName) throws IOException {
        File imageFile = new File(filePath, fileName); // 파일 경로와 파일명으로 파일을 찾기
        if (!imageFile.exists()) {
            throw new IOException("File not found: " + filePath + "/" + fileName);
        }
        // 파일이 이미지인지 확인
        if (!fileValidator.isValidImage(fileValidator.getFileExtension(fileName))) {
            throw new IllegalArgumentException("File is not an image, cannot generate thumbnail.");
        }
        // 이미지를 썸네일 크기로 변환
        return Thumbnails.of(imageFile)
//                .sourceRegion(Positions.CENTER, THUMBNAIL_REGION_WIDTH_PX, THUMBNAIL_REGION_HEIGHT_PX)
                .size(THUMBNAIL_WIDTH_PX, THUMBNAIL_HEIGHT_PX)  // 썸네일 크기 지정
                .asBufferedImage();  // BufferedImage로 반환
    }

    // 생성된 썸네일을 지정된 디렉토리에 저장하는 메서드
    @Override
    public void saveThumbnail(MultipartFile file, FileStoragePathEnum fileStorageType) throws IOException {
        // 파일이 이미지인지 확인
        if (!fileValidator.isValidImage(file)) {
            throw new IllegalArgumentException("File is not an image, cannot generate thumbnail.");
        }
        // 파일 이름에서 확장자 추출
        String filename = file.getOriginalFilename();
        String fileExtension = fileValidator.getFileExtension(filename);

        // 파일 저장 경로 (확장자는 동적으로 결정)
        File outputFile = new File(fileStorageType.getStoragePath(null), file.getOriginalFilename());
        Files.createDirectories(Paths.get(outputFile.getParent()));

        // 썸네일 이미지 저장
        ImageIO.write(generateThumbnail(file), fileExtension, outputFile);
    }

    // 생성된 썸네일을 지정된 디렉토리에 저장하는 메서드
    @Override
    public void saveThumbnailWithFileName(MultipartFile file, FileStoragePathEnum fileStorageType, String fileName) throws IOException {
        // 파일이 이미지인지 확인
        if (!fileValidator.isValidImage(file)) {
            throw new IllegalArgumentException("File is not an image, cannot generate thumbnail.");
        }
        // 파일 이름에서 확장자 추출
        String filename = file.getOriginalFilename();
        String fileExtension = fileValidator.getFileExtension(filename);

        // 파일 저장 경로 (확장자는 동적으로 결정)
        File outputFile = new File(fileStorageType.getStoragePath(null), fileName + "." + fileExtension);
        Files.createDirectories(Paths.get(outputFile.getParent()));

        // 썸네일 이미지 저장
        ImageIO.write(generateThumbnail(file), fileExtension, outputFile);
    }
}