package egovframework.kt.dxp.api.domain.notice;

import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.notice.record.NoticeDetailSearchRequest;
import egovframework.kt.dxp.api.domain.notice.record.NoticeDetailSearchResponse;
import egovframework.kt.dxp.api.domain.notice.record.NoticeSearchRequest;
import egovframework.kt.dxp.api.domain.notice.record.NoticeSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공지사항 Controller
 *
 * @author MINJI
 * @since 2024-10-15
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "[CDA-ARA] 공지사항 조회", description = "[담당자 : MINJI]")
public class NoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "[CDA-ARA-005] 공지사항 목록 조회", notes = """
            # Searchable Field
            - createDate [생성 일시 : between/yyyyMMddHHmmss]
            - 공지사항 확인 기한 : 1년 (정책 미정)
            """)
    @PostMapping(value = "/v1/notice/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GridResponse<NoticeSearchResponse>> getSearchNoticeList(
            @RequestBody NoticeSearchRequest request) {
        return ResponseEntity.ok()
                .body(noticeService.getSearchNoticeList(request));
    }

    @ApiOperation(value = "[CDA-ARA-006] 공지사항 상세 조회")
    @PostMapping(value = "/v1/notice/detail/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<NoticeDetailSearchResponse>> getDetailSearchNotice(
            @RequestBody NoticeDetailSearchRequest noticeDetailSearchRequest) {
        return ResponseEntity.ok()
                .body(noticeService.getDetailSearchNotice(noticeDetailSearchRequest));
    }

    //@ApiOperation(value = "[CDA-ARA-007] 공지사항 파일 다운로드")
    //@PostMapping(value = "/v1/notice/file-download", consumes = MediaType.APPLICATION_JSON_VALUE)
    //public void getDownloadFile(@RequestBody FileDownloadRequest parameter, HttpServletResponse httpServletResponse) {
    //    noticeService.getDownloadFile(parameter, httpServletResponse);
    //}
}
