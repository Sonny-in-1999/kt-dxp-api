package egovframework.kt.dxp.api.domain.sendMessage;

import egovframework.kt.dxp.api.config.firebase.PushNotification;
import egovframework.kt.dxp.api.config.firebase.enumeration.MessageDivision;
import egovframework.kt.dxp.api.config.firebase.record.MessageRequest;
import egovframework.kt.dxp.api.config.firebase.record.NotificationResultResponse;
import egovframework.kt.dxp.api.config.firebase.record.PushTokenRequest;
import egovframework.kt.dxp.api.domain.sendMessage.record.SendMessageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * @author GEONLEE
 * @since 2024-09-12
 */
@Service
@Slf4j
@DependsOn("applicationContextHolder")
public class SendMessageService {

    public SendMessageResponse send(MessageRequest messageRequest) {
        log.info("send message test");
        List<PushTokenRequest> list = new ArrayList<>();

        list.add(PushTokenRequest.builder()
                                .pushToken(messageRequest.pushToken())
//                .pushToken("e1FIymhUQ0Sl4ZFpXwsSz4:APA91bEJnP5i6W_Ap559IaAcVRd7Ytu0NWkkDCfRov9gCzrR6rIeoWyiGVKeKGYOMCrr03QR9i_xg_5nrEdajwj0ddZxF2yR2e783wejw0OMyjStBIf5FIBblm_8iq1jqDoHZE4xYICq") //AOS
//                .pushToken("cSA8GK_UiEwvn7Ya0z3fd7:APA91bEWFw6RdHyNfL3cortA0oIwEkcraQUtlqyfBtPAjl7qZxTNwcpw-PPXbYOLm-HWg-ThnTm5vW5xPxzSWsDKUQEMsvqaS8mmuBNzkJdvsYgSB1M6UPzBr6DirKAqbZKqys4t1tMq") //IOS
                .userId("user01")
                .build());
        PushNotification pushNotification = new PushNotification(
                messageRequest.message(),
                MessageDivision.NOTIFICATION,
                "https://velog.io/@geonlee/posts",
                list
        );
        try {
            List<NotificationResultResponse> resultList = pushNotification.sendMessage();
            /*
             * TODO 2024-09-12 전송 이력을 관리한다면 getResult() 로 결과를 조회해 갱신한다.
             *
             * */
        } catch (ExecutionException | InterruptedException e) {
            log.error("Firebase 알림 전송 결과를 얻는데 실패하였습니다.", e);
            throw new RuntimeException(e);
        }

        return SendMessageResponse.builder()
                .build();
    }
}
