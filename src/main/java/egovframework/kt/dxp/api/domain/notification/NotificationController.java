package egovframework.kt.dxp.api.domain.notification;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyRequest;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyResponse;
import egovframework.kt.dxp.api.domain.notification.record.NotificationSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NotificationController
 * Description   :  
 * Creation Date : 2024-10-15
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-15, Juyoung Chae, 최초작성
 *                 2 - 2024-10-22, BITNA, 알림 설정 조회 및 수정 구현
 ******************************************************************************************/
@RestController
//@Tag(name = "[CDA-ARA] 알림 설정", description = "[담당자: BITNA]")
@Api(tags = "[CDA-ARA] 알림 설정", description = "[담당자: BITNA]")
//@SecurityRequirement(name = "JWT Token")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    //    @Operation(summary = "[CDA-ARA-001] 알림 설정 수정", description = "알림 설정를 수정합니다.")
    @ApiOperation(value = "[CDA-ARA-001] 알림 설정 수정", notes = """
            # Parameters
            - actAlarmYn [활동 알람 여부]
            - noticeAlarmYn [공지 알람 여부]
            - rewardAlarmYn [혜택(보상) 알람 여부]
            """)
    @PostMapping(value = "/v1/notification/modify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse<NotificationModifyResponse>> modifyNotification(@RequestBody @Valid NotificationModifyRequest parameter) {
        return ResponseEntity.ok()
                             .body(notificationService.modifyNotification(parameter));
    }

    // 토큰?에서 userId 가져오는 로직 추가 필요
    @PostMapping(value = "/v1/notification/list/search", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "[CDA-ARA-001] 알림 설정 목록 조회", description = """
//            # Parameters
//            - ntPushAlarmYn [야간 푸시 알람 여부]
//            - actAlarmYn [활동 알람 여부]
//            - noticeAlarmYn [공지 알람 여부]
//            - mctAlarmYn [혜택(보상) 알람 여부]
//            """,
//            operationId = "[##API ID]"
//    )
    @ApiOperation(value = "[CDA-ARA-002] 알림 설정 목록 조회", notes = """
            # Response
            - actAlarmYn [활동 알람 여부]
            - noticeAlarmYn [공지 알람 여부]
            - rewardAlarmYn [혜택(보상) 알람 여부]
            ""\",
            operationId = "[CDA-ARA-001]"
            """)
    public ResponseEntity<ItemResponse<NotificationSearchResponse>> getAlram() {
        return ResponseEntity.ok()
                .body(notificationService.getNotification());
    }

}
