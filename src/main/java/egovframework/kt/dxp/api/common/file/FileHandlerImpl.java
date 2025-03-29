package egovframework.kt.dxp.api.common.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public interface FileHandlerImpl {
    void uploadFile(MultipartFile file, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException;
    void uploadFile(List<MultipartFile> files, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException;
    void uploadFileWithThumbnail(MultipartFile file, String fileName, FileStoragePathEnum originalPath, FileStoragePathEnum thumbnailPath, String destinationDir) throws IOException;
    void uploadFileWithFileName(MultipartFile file, String fileName, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException;
    void uploadFileWithFileName(List<MultipartFile> files, List<String> fileNames, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException;

    Resource downloadResourceFile(String fullPathWithFileName) throws IOException;
    Resource downloadResourceFile(String fileName, String destinationDir) throws IOException;
    InputStream downloadInputStreamFile(String fullPathWithFileName) throws IOException;
    InputStream downloadInputStreamFile(String fileName, String destinationDir) throws IOException;

    BufferedImage generateThumbnail(MultipartFile file) throws IOException;;
    BufferedImage generateThumbnail(String filePath, String fileName) throws IOException;
    void saveThumbnail(MultipartFile file, FileStoragePathEnum fileStorageType) throws IOException;
    void saveThumbnailWithFileName(MultipartFile file, FileStoragePathEnum fileStorageType, String fileName) throws IOException;

//    boolean canGenerateThumbnail(MultipartFile file);
}
