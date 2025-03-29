package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageCheckRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSearchRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceController
 * Description   : 알림 조회 Controller
 * Creation Date : 2024-10-18
 * Written by    : MinJi Chae Senior Researcher
 * History       : 1 - 2024-10-18, MinJi Chae, 최초작성
 ******************************************************************************************/
@RestController
@RequiredArgsConstructor
@Api(tags = "[CDA-ARA] 알림 조회 ", description = "[담당자: MINJI]")
public class PushMessageController {

    private final PushMessageService pushMessageService;

    @PostMapping(value = "/v1/alarm/list/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-ARA-003] 알림 목록 조회", notes = """
            - pageNo [Current page number : 0]
            - pageSize [Number of data in page : 10]
            - 알림 확인 기한 : 30일 (정책 미정)
            """)
    public ResponseEntity<GridResponse<PushMessageSearchResponse>> getSearchPushMessageList(@RequestBody PushMessageSearchRequest request) {
        return ResponseEntity.ok()
                .body(pushMessageService.getSearchPushMessageList(request));
    }


    @PostMapping(value = "/link/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-ARA-004] 알림 확인", notes = """
            - createDate [생성 일시: 2024-10-30 14:40:55]
            - messageType [메시지 구분: A01]
            - link [http://XXXX.XXXX.XXXX]
            - 알림 확인 시 '알림 확인 여부 = Y'로 변경하기 위한 API
            """)
    public ResponseEntity<ItemResponse<Long>> onNotificationTouchedAndRedirect(@RequestBody PushMessageCheckRequest request) {
        return ResponseEntity.ok()
                .body(pushMessageService.notificationActionTrigger(request));
    }

    //navigateAfterNotification
}
