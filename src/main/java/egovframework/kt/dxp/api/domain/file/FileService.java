package egovframework.kt.dxp.api.domain.file;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.AbstractFileHandler;
import egovframework.kt.dxp.api.common.file.FilePreviewService;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.file.record.FilePreviewRequest;
import egovframework.kt.dxp.api.domain.file.record.FilePreviewResponse;
import egovframework.kt.dxp.api.domain.file.record.FileRequest;
import egovframework.kt.dxp.api.entity.L_FILE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@DependsOn("applicationContextHolder")
@RequiredArgsConstructor
public class FileService extends AbstractFileHandler {

    private final FileRepository fileRepository;
    private final FilePreviewService filePreviewService = new FilePreviewService();
    private final MessageConfig messageConfig;

//    @Override
//    public void uploadFile(List<MultipartFile> files, FileStoragePathEnum fileStorageType, String destinationDir) throws IOException {
//
//        super.uploadFile(files, fileStorageType, destinationDir);
//    }

    @Transactional
    public ResponseEntity<Resource> getDownloadFile(FileRequest request) throws IOException {
        if (request.bulletinBoardDivision() == null || request.saveFileName() == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndSaveFileName(request.bulletinBoardDivision(), request.saveFileName());

        // 첫 번째 값만 사용
        L_FILE entity = fileList.stream()
                .findFirst() // 첫 번째 요소만 가져오기
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        // 실제 파일을 Path로 가져옴
        String encodedOriginalFileName = UriUtils.encode(CommonUtils.combineFileNameAndExtension(request.saveFileName(), entity.getFileExtension()), StandardCharsets.UTF_8);
        Resource resource = super.downloadResourceFile(encodedOriginalFileName, entity.getFilePath());
        Path path = resource.getFile().toPath();

        // 헤더 설정
        HttpHeaders headers = createDownloadHeaders(encodedOriginalFileName, Files.size(path));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);  // 파일을 읽고 전송
    }

    @Transactional
    public ResponseEntity<Resource> getDownloadFile(String bulletinBoardDivision, String saveFileName, String fileRequestType) throws IOException {
        ImageRequest fileRequest = null;
        Resource resource = null;

        if(bulletinBoardDivision == null || saveFileName == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        if(ImageRequest.isValid(fileRequestType)) {
            fileRequest = ImageRequest.valueOf(fileRequestType.toUpperCase());
        }

        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndSaveFileName(bulletinBoardDivision, saveFileName);

        // 첫 번째 값만 사용
        L_FILE entity = fileList.stream()
                .findFirst() // 첫 번째 요소만 가져오기
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

//        L_FILE entity = fileRepository.findByBulletinBoardDivisionAndSaveFileName(bulletinBoardDivision, saveFileName)
//                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        String encodedOriginalFileName = UriUtils.encode(CommonUtils.combineFileNameAndExtension(entity.getActualFileName(), entity.getFileExtension()), StandardCharsets.UTF_8);

        resource = (fileRequest == null) ?
                super.downloadResourceFile(CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()), entity.getFilePath()) :
                switch (fileRequest) {
                    case THUMBNAIL -> super.downloadResourceFile(CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()), entity.getThumbnailFilePath());
                    case ORIGINAL -> super.downloadResourceFile(CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()), entity.getFilePath());
                };

        if (resource == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        Path path = resource.getFile().toPath();

        // 헤더 설정
        HttpHeaders headers = createDownloadHeaders(encodedOriginalFileName, Files.size(path));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);  // 파일을 읽고 전송
    }

    @Transactional
    public ItemResponse<FilePreviewResponse> getPreviewFile(FilePreviewRequest request) throws IOException {
        ImageRequest imageRequest;
        String imageByte = null;

        if (request.bulletinBoardDivision() == null || request.saveFileName() == null) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        if (ImageRequest.isValid(request.imageRequest())) {
            imageRequest = ImageRequest.valueOf(request.imageRequest().toUpperCase());
        } else {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndSaveFileName(request.bulletinBoardDivision(), request.saveFileName());

        // 첫 번째 값만 사용
        L_FILE entity = fileList.stream()
                .findFirst() // 첫 번째 요소만 가져오기
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        switch (imageRequest) {
            case ORIGINAL:
                // NOTE: 이미지는 원본으로 넘어가는데 PDF나 TEXT는 요약본으로 세팅 됨
                try {
                    imageByte = filePreviewService.getOriginal(entity.getFilePath(), CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()));
                } catch(IOException e) {
                    imageByte = null;
                }
                break;
            case THUMBNAIL:
                try {
                    imageByte = filePreviewService.getPreview(entity.getFilePath(), CommonUtils.combineFileNameAndExtension(entity.getSaveFileName(), entity.getFileExtension()));
                } catch(IOException e) {
                    imageByte = null;
                }
                break;
        }
        FilePreviewResponse previewResponse = FilePreviewResponse.builder()
               .imagePreview(imageByte)
               .build();

        return ItemResponse.<FilePreviewResponse>builder()
               .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
               .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
               .item(previewResponse)
               .build();
    }
//
//    //    // 파일을 저장하고 URL 반환
////    public String uploadFile(byte[] fileBytes, String filename) throws IOException {
////        // 경로 구분자를 시스템에 맞게 처리
////        Path uploadPath = Paths.get(uploadDir, filename);
////
////        // 파일을 지정한 경로에 저장
////        try (OutputStream os = new FileOutputStream(uploadPath.toFile())) {
////            os.write(fileBytes);
////        }
////
////        // 파일 저장 완료 후 URL 반환 (예: http://localhost:8080/api/files/download/파일명)
////        return "/api/files/download/" + filename;
////    }
//
//    @Transactional
//    public ResponseEntity<byte[]> getDownloadFile(FileRequest request) {
//
//        if (request.bulletinBoardDivision() == null || request.saveFileName() == null) {
//            throw new ServiceException(ErrorCode.NO_DATA);
//        }
//
//        L_FILE entity = fileRepository.findByBulletinBoardDivisionAndSaveFileName(request.bulletinBoardDivision(), request.saveFileName())
//                                      .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
//        try {
//
//            // 파일 읽기 (스트리밍 처리)
//            Path path = this.getFilePath(entity.getFilePath() + "/" + entity.getActualFileName() + "." + entity.getFileExtension());
//            String encodedOriginalFileName = UriUtils.encode(entity.getActualFileName() + "." + entity.getFileExtension(), StandardCharsets.UTF_8);
//
//            // 스트리밍 처리
//            try (InputStream inputStream = Files.newInputStream(path)) {
//                // 헤더 설정
//                HttpHeaders headers = createDownloadHeaders(encodedOriginalFileName, Files.size(path));
//
//                return ResponseEntity.ok()
//                                     .headers(headers)
//                                     .body(inputStream.readAllBytes());  // 파일을 읽고 전송
//            }
//        } catch (IOException e) {
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
//        }
//
//    }
//
//
//    @Transactional
//    public ResponseEntity<byte[]> getDownloadFile(String bulletinBoardDivision, String saveFileName) {
//
//        if (bulletinBoardDivision == null || saveFileName == null) {
//            throw new ServiceException(ErrorCode.NO_DATA);
//        }
//
//        L_FILE entity = fileRepository.findByBulletinBoardDivisionAndSaveFileName(bulletinBoardDivision, saveFileName)
//                                      .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
//        try {
//            // 파일 읽기 (스트리밍 처리)
//            Path path = this.getFilePath(entity.getFilePath());
//            String encodedOriginalFileName = UriUtils.encode(combineFileNameAndExtension(saveFileName, entity.getFileExtension()), StandardCharsets.UTF_8);
//
//            // 파일 경로 확인
//            if (!this.fileExists(entity.getFilePath())) {
//                throw new ServiceException(ErrorCode.NO_DATA);
//            }
//
//
////            String encodedOriginalFileName = UriUtils.encode(entity.getActualFileName() + "." + entity.getFileExtension(), StandardCharsets.UTF_8);
//
//            // 스트리밍 처리
//            try (InputStream inputStream = Files.newInputStream(path)) {
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("Content-Disposition", "attachment; filename=" + encodedOriginalFileName);
//                headers.add("Content-Type", "application/octet-stream");
//                headers.add("Content-Length", String.valueOf(Files.size(path)));
//
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .body(inputStream.readAllBytes());  // 파일을 읽고 전송
//            }
//        } catch (IOException e) {
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
//        }
//
//    }
    //// 합쳐진 경로로부터 파일 이름을 추출하고 경로를 분리하는 메서드
    //public String extractFileNameFromPath(String fullPath) {
    //    // fullPath가 이미 경로와 파일명으로 합쳐져 있을 때
    //    Path path = Paths.get(fullPath);
    //
    //    // 파일 이름 추출
    //    String filename = path.getFileName().toString();
    //
    //    return filename;
    //}

    // 파일 다운로드를 위한 경로 반환
    public Path getFilePath(String fullPath) {
        return Paths.get(fullPath);
    }

    // 경로가 올바른지 검사 (파일 존재 여부 확인)
    public boolean fileExists(String filename) {
        Path path = getFilePath(filename);
        return Files.exists(path);
    }

    private HttpHeaders createDownloadHeaders(String fileName, long fileSize) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Length", String.valueOf(fileSize));
        return headers;
    }
    public enum ImageRequest {
        ORIGINAL,     // 원본 이미지
        THUMBNAIL;    // 썸네일 이미지

        // enum에서 String 값이 유효한지 확인하는 메서드 추가
        public static boolean isValid(String value) {
            for (ImageRequest division : ImageRequest.values()) {
                if (division.name().equalsIgnoreCase(value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
