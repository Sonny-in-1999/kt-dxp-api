package egovframework.kt.dxp.api.domain.file;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.file.record.FilePreviewRequest;
import egovframework.kt.dxp.api.domain.file.record.FilePreviewResponse;
import egovframework.kt.dxp.api.domain.file.record.FileRequest;
import egovframework.kt.dxp.api.domain.notice.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(tags = "[CDA-COM] 파일 업로드 / 다운로드", description = "[담당자:  Juyoung Chae]")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final NoticeService noticeService;

//    @PostMapping(value = "/v1/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "[CDA-COM-004] 파일 업로드")
//    public ResponseEntity<String> uploadFile(@RequestPart(required = false, name = "fileList") List<MultipartFile> files, FileStoragePathEnum fileStorageType, String destinationDir){
//        try {
//            fileService.uploadFile(files, fileStorageType, destinationDir);
//            return ResponseEntity.ok("File uploaded successfully.");
//        } catch(IOException e) {
//            return ResponseEntity.ok("File uploaded Error.");
//        }
//    }

//    @PostMapping(value = "/v1/file/download", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "[CDA-COM-004] 파일 다운로드")
//    //public ResponseEntity<ItemsResponse<FileResponse>> downloadFile(@RequestBody @Valid FileRequest request) {
//        public ResponseEntity<byte[]> downloadFile(@RequestBody @Valid FileRequest request) {
//
//        return fileService.getDownloadFile(request);
//    }
//
//    @GetMapping(value = "/v1/file/download", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "[CDA-COM-004] 파일 다운로드")
//    //public ResponseEntity<ItemsResponse<FileResponse>> downloadFile(@RequestBody @Valid FileRequest request) {
//    public ResponseEntity<byte[]> downloadFile(@RequestParam @Valid String bulletinBoardDivision, @RequestParam @Valid String saveFileName) {
//
//        return fileService.getDownloadFile(bulletinBoardDivision, saveFileName);
//    }

    @PostMapping(value = "/v1/file/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-COM-004] 파일 다운로드")
    public ResponseEntity<Resource> downloadFile(@RequestBody @Valid FileRequest request) throws IOException {

        return fileService.getDownloadFile(request);
    }

    @GetMapping(value = "/v1/file/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-COM-004] 파일 다운로드")
    public ResponseEntity<Resource> downloadFile(@RequestParam @Valid String bulletinBoardDivision, @RequestParam @Valid String saveFileName, @RequestParam(required = false) String fileRequestType) throws IOException {

        return fileService.getDownloadFile(bulletinBoardDivision, saveFileName, fileRequestType);
    }

    @PostMapping(value = "/v1/file/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-COM-004] 파일 정보 미리보기")
    public ResponseEntity<ItemResponse<FilePreviewResponse>> previewFile(@RequestBody @Valid FilePreviewRequest request) throws IOException {
        return ResponseEntity.ok()
                .body(fileService.getPreviewFile(request));
    }
}
