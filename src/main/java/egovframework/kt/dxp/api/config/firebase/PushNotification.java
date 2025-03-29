package egovframework.kt.dxp.api.config.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import egovframework.kt.dxp.api.config.firebase.enumeration.MessageDivision;
import egovframework.kt.dxp.api.config.firebase.record.NotificationRequest;
import egovframework.kt.dxp.api.config.firebase.record.NotificationResultResponse;
import egovframework.kt.dxp.api.config.firebase.record.PushTokenRequest;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author GEONLEE
 * @since 2024-09-12
 * Use SendMessageExecutor
 */
@Slf4j
@Deprecated
public class PushNotification {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final String message;
    private final MessageDivision messageDivision;
    private final String link;
    private final List<PushTokenRequest> pushTokenRequestList;

    public PushNotification(String message, MessageDivision messageDivision, String link, List<PushTokenRequest> pushTokenRequestList) {
        this.message = message;
        this.messageDivision = messageDivision;
        this.link = link;
        this.pushTokenRequestList = pushTokenRequestList;
    }

    public List<NotificationResultResponse> sendMessage() throws ExecutionException, InterruptedException {
        if (CollectionUtils.isEmpty(this.pushTokenRequestList)) {
            log.error("There is no destination to send the message to.");
            return null;
        }
        Future<List<NotificationResultResponse>> future = executorService.submit(() -> {
            List<NotificationResultResponse> resultList = new ArrayList<>();
            List<String> pushTokenList = this.pushTokenRequestList.stream().map(PushTokenRequest::pushToken).toList();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String title = "KT 춘천시 DX 플랫폼";
                MulticastMessage multicastMessage = MulticastMessage.builder()
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(this.message)
                                .build())
                        .putData("body", objectMapper.writeValueAsString(
                                NotificationRequest.builder()
                                        .title(title)
                                        .message(this.message)
//                                        .messageDivision(this.messageDivision)
                                        .link(this.link)
                                        .build()))
                        .addAllTokens(pushTokenList)
                        .build();
                BatchResponse batchResponse = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
                List<SendResponse> sendResponses = batchResponse.getResponses();
                for (int i = 0, n = sendResponses.size(); i < n; i++) {
                    if (sendResponses.get(i).isSuccessful()) {
                        resultList.add(NotificationResultResponse.builder()
                                .userId(this.pushTokenRequestList.get(i).userId())
                                .pushToken(this.pushTokenRequestList.get(i).pushToken())
                                .sendYn(TransmissionDivision.Y)
                                .build());
                    } else {
                        resultList.add(NotificationResultResponse.builder()
                                .userId(this.pushTokenRequestList.get(i).userId())
                                .pushToken(this.pushTokenRequestList.get(i).pushToken())
                                .sendYn(TransmissionDivision.N)
                                .exceptionCode(sendResponses.get(i).getException().getErrorCode())
                                .exceptionMessage(sendResponses.get(i).getException().getMessage())
                                .build());
                    }
                }
                log.info("Result of request to send message.\n Total: {}\n Success: {}\n Failure: {}",
                        pushTokenList.size(), batchResponse.getSuccessCount(), batchResponse.getFailureCount());
                return resultList;
            } catch (JsonProcessingException e) {
                log.error("Fail to convert record to Json.");
                throw new RuntimeException(e);
            } catch (FirebaseMessagingException e) {
                log.error("Fail to send message.");
                throw new RuntimeException(e);
            }
        });
        return future.get();
    }
}
