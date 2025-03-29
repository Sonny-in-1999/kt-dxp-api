package egovframework.kt.dxp.api.domain.sendMessage;

import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.config.firebase.record.MessageRequest;
import egovframework.kt.dxp.api.domain.sendMessage.record.SendMessageResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GEONLEE
 * @since 2024-09-11
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "02.message 전송")
@Slf4j
public class SendMessageController {

    private final MessageConfig messageConfig;
    private final SendMessageService sendMessageService;

    @PostMapping(value = "/send-message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "FCM message 전송 테스트")
    public ResponseEntity<ItemResponse<SendMessageResponse>> sendMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok()
                .body(ItemResponse.<SendMessageResponse>builder()
                        .status(messageConfig.getCode(NormalCode.REQUEST_SUCCESS))
                        .message(messageConfig.getMessage(NormalCode.REQUEST_SUCCESS))
                        .item(sendMessageService.send(messageRequest))
                        .build());
    }
}
