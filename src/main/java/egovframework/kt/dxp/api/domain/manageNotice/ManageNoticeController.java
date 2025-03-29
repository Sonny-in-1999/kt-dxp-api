package egovframework.kt.dxp.api.domain.manageNotice;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateRequest;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeCreateResponse;
import egovframework.kt.dxp.api.domain.manageNotice.record.NoticeDeleteRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "[CDA-ARA] 공지사항 관리", description = "[담당자 : MINJI]")
public class ManageNoticeController {

    private final ManagerNoticeService managerNoticeService;

    @ApiOperation(value = "[CDA-ARA-008] 공지사항 추가")
    @PostMapping(value = "/v1/notice/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<NoticeCreateResponse>> createNotice(
            @Valid @RequestPart(name = "noticeCreateRequest") NoticeCreateRequest parameter, @RequestPart(required = false, name = "fileList") List<MultipartFile> fileList)  throws IOException {
        return ResponseEntity.ok()
                .body(managerNoticeService.createNotice(parameter, fileList));
    }

    //@ApiOperation(value = "[CDA-ARA-007] 공지사항 수정")
    //@PostMapping(value = "/v1/notice/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<ItemResponse<NoticeCreateResponse>> createNotice(
    //        @Valid @RequestPart(name = "noticeCreateRequest") NoticeCreateRequest parameter, @RequestPart(required = false, name = "fileList") List<MultipartFile> fileList) {
    //    return ResponseEntity.ok()
    //            .body(managerNoticeService.createNotice(parameter, fileList));
    //}

    @ApiOperation(value = "[CDA-ARA-011] 공지사항 삭제")
    @PostMapping(value = "/v1/notice/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<Long>> deleteOperator(@RequestBody @Valid NoticeDeleteRequest parameter) {
        return ResponseEntity.ok()
                .body(managerNoticeService.deleteNotice(parameter));
    }

}
