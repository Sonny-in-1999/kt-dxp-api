package egovframework.kt.dxp.api.config.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import egovframework.kt.dxp.api.config.firebase.record.MessageRequest;
import egovframework.kt.dxp.api.config.firebase.record.MulticastMessageRequest;
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

/**
 * @author GEONLEE
 * @since 2024-10-29
 */
@Slf4j
public class SendMessageExecutor {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * multicast message 전송
     * 하나의 MulticastMessage 로 여러 기기에 동일한 메시지 전송해야 할 때 사용
     */
    public List<NotificationResultResponse> sendMulticastMessage(MulticastMessageRequest multicastMessageRequest)
            throws ExecutionException, InterruptedException {
        List<String> pushTokenList = multicastMessageRequest.pushTokenRequestList()
                .stream().map(PushTokenRequest::pushToken).toList();
        Future<List<NotificationResultResponse>> future = executorService.submit(() -> {
            try {
                MulticastMessage multicastMessage = MulticastMessage.builder()
                        .setNotification(
                                Notification.builder()
                                        .setTitle(multicastMessageRequest.title())
                                        .setBody(multicastMessageRequest.message())
                                        .build())
                        .putData("body", this.objectMapper.writeValueAsString(
                                NotificationRequest.builder()
                                        .title(multicastMessageRequest.title())
                                        .message(multicastMessageRequest.message())
                                        .messageDivision(multicastMessageRequest.messageDivision())
                                        .link(multicastMessageRequest.link())
                                        .build()))
                        .addAllTokens(pushTokenList)
                        .build();
                BatchResponse batchResponse = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
                return getMessageResponse(multicastMessageRequest.pushTokenRequestList(), batchResponse);
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

    /**
     * message 전송
     * 각각의 Message 객체를 만들어 개별적으로 전송
     */
    public List<NotificationResultResponse> sendMessage(List<MessageRequest> messageRequestList)
            throws ExecutionException, InterruptedException {

        Future<List<NotificationResultResponse>> future = executorService.submit(() -> {
            try {
                List<Message> messages = messageRequestList.stream().map(messageRequest -> {
                    try {
                        return Message.builder()
                                .setNotification(Notification.builder()
                                        .setTitle(messageRequest.title())
                                        .setBody(messageRequest.message())
                                        .build())
                                .putData("body", this.objectMapper.writeValueAsString(
                                        NotificationRequest.builder()
                                                .title(messageRequest.title())
                                                .message(messageRequest.message())
                                                .messageDivision(messageRequest.messageType())
                                                .link(messageRequest.link())
                                                .build()))
                                .setToken(messageRequest.pushToken())
                                .build();
                    } catch (JsonProcessingException e) {
                        log.error("Fail to convert record to Json.");
                        throw new RuntimeException(e);
                    }
                }).toList();
                List<PushTokenRequest> pushTokenRequestList = messageRequestList.stream()
                        .map(messageRequest -> PushTokenRequest.builder()
                                .userId(messageRequest.userId())
                                .pushToken(messageRequest.pushToken())
                                .build()).toList();
                BatchResponse batchResponse = FirebaseMessaging.getInstance().sendEach(messages);
                return getMessageResponse(pushTokenRequestList, batchResponse);
            } catch (FirebaseMessagingException e) {
                log.error("Fail to send message.");
                throw new RuntimeException(e);
            }
        });
        return future.get();
    }

    /**
     * firebase 로 부터 받은 메시지 결과를 리턴
     */
    private List<NotificationResultResponse> getMessageResponse(List<PushTokenRequest> pushTokenRequestList, BatchResponse batchResponse) {
        List<NotificationResultResponse> resultList = new ArrayList<>();
        List<SendResponse> sendResponses = batchResponse.getResponses();
        for (int i = 0, n = sendResponses.size(); i < n; i++) {
            if (sendResponses.get(i).isSuccessful()) {
                resultList.add(NotificationResultResponse.builder()
                        .userId(pushTokenRequestList.get(i).userId())
                        .pushToken(pushTokenRequestList.get(i).pushToken())
                        .sendYn(TransmissionDivision.Y)
                        .build());
            } else {
                resultList.add(NotificationResultResponse.builder()
                        .userId(pushTokenRequestList.get(i).userId())
                        .pushToken(pushTokenRequestList.get(i).pushToken())
                        .sendYn(TransmissionDivision.N)
                        .exceptionCode(sendResponses.get(i).getException().getErrorCode())
                        .exceptionMessage(sendResponses.get(i).getException().getMessage())
                        .build());
            }
        }
        log.info("Result of request to send message.\n Total: {}\n Success: {}\n Failure: {}",
                pushTokenRequestList.size(), batchResponse.getSuccessCount(), batchResponse.getFailureCount());
        return resultList;
    }
}
