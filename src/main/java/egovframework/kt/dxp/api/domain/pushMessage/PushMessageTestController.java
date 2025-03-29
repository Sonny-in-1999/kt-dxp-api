package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSaveRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSaveResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "[CDA-TST] 알림메시지 테스트", description = "[담당자: Minsu Son]")
public class PushMessageTestController {


    private final PushMessageService pushMessageService;


    @PostMapping(value = "/v1/alarm/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-TST-001] 테스트용 알림메시지 추가")
    public ResponseEntity<ItemResponse<PushMessageSaveResponse>> savePushMessage(
            @RequestBody @Valid PushMessageSaveRequest request
    ) {
        ItemResponse<PushMessageSaveResponse> savedPushMessage
                = pushMessageService.savePushMessage(request);
        return ResponseEntity.ok().body(savedPushMessage);
    }
}
