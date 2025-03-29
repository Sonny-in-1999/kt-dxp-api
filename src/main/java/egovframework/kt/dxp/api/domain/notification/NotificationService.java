package egovframework.kt.dxp.api.domain.notification;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.alarm.AlarmRepository;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyRequest;
import egovframework.kt.dxp.api.domain.notification.record.NotificationModifyResponse;
import egovframework.kt.dxp.api.domain.notification.record.NotificationSearchResponse;
import egovframework.kt.dxp.api.entity.C_ALARM;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
/**
 * 알림 설정 Service
 *
 * @author BITNA
 * @apiNote 2024-10-28 BITNA getUserId mapper에서 추가하도록 변경
 * @since 2024-10-21<br />
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final AlarmRepository alarmRepository;
    private static final NotificationMapper alramMapper = NotificationMapper.INSTANCE;
    private final MessageConfig messageConfig;

    /**
     * 알림 설정 정보 리스트 조회
     *
     * @param parameter 알림 설정 조회 조건
     * @return AlramSearchResponse 알림 설정 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-21<br />
     */
    @Transactional
    public ItemResponse<NotificationSearchResponse> getNotification() {
        String userId = CommonUtils.getUserId();

        /*queryMethod or querydsl or namedNativeQuery 작성*/
        C_ALARM cAlarm = alarmRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorCode.NO_DATA)
        );

        return ItemResponse.<NotificationSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(alramMapper.toSearchResponse(cAlarm))
                .build();
    }

    /**
     * 알림 설정 정보 수정
     *
     * @param parameter 알림 설정 수정 요청 정보
     * @return AlramModifyResponse 수정된 알림 설정 정보 응답
     * @author BITNA
     * @since 2024-10-21<br />
     */
    @Transactional
    public ItemResponse<NotificationModifyResponse> modifyNotification(NotificationModifyRequest parameter) {

        String userId = CommonUtils.getUserId();
        C_ALARM entity = alarmRepository.findById(userId)
                                        .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
        C_ALARM modifiedEntity = alramMapper.updateFromRequest(parameter, entity);
        alarmRepository.saveAndFlush(modifiedEntity);

        return ItemResponse.<NotificationModifyResponse>builder()
                .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                .item(alramMapper.toModifyResponse(modifiedEntity))
                .build();
    }
}
