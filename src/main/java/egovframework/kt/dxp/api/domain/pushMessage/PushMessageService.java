package egovframework.kt.dxp.api.domain.pushMessage;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageCheckRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSaveRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSaveResponse;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSearchRequest;
import egovframework.kt.dxp.api.domain.pushMessage.record.PushMessageSearchResponse;
import egovframework.kt.dxp.api.entity.L_PUSH_MSG;
import egovframework.kt.dxp.api.entity.code.MSG_TYPE;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_PUSH_MSG_KEY;
import egovframework.kt.dxp.api.entity.key.M_CD_KEY;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 알림 조회 Service <br />

 * @author Juyoung
 * @since 2024-11-05
 * @Modification 2024-11-05 Juyoung - 최초작성<br /> */
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class PushMessageService {

    private static final Integer LIST_DISPLAY_PERIOD = 30;
    private final PushMessageRepository pushMessageRepository;
    private final MessageTypeRepository messageTypeRepository;
    private static final PushMessageMapper pushMessageMapper = PushMessageMapper.INSTANCE;
    private final MessageConfig messageConfig;

    ///**
    // * 알림 목록 조회(DynamicRequest 방식)
    // *
    // * @param parameter 알림 조회 조건
    // * @return AlarmSearchResponse 알림 조회 결과
    // * @author MINJI
    // * @since 2024-10-16<br />
    // */
    //@Transactional
    //public ItemsResponse<PushMessageSearchResponse> getSearchPushMessageList(DynamicRequest parameter) {
    //    Page<L_PUSH_MSG> page = pushMessageRepository.findDynamicWithPageable(parameter);
    //    if (CollectionUtils.isEmpty(page.getContent())) {
    //        throw new ServiceException(ErrorCode.NO_DATA);
    //    }
    //    List<PushMessageSearchResponse> list = pushMessageMapper.toSearchResponseList(page.getContent());
    //
    //    return ItemsResponse.<PushMessageSearchResponse>builder()
    //            .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
    //            .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
    //            .items(list)
    //            .build();
    //}

    /**
     *
     * 알림 목록 조회 <br />
     * @param parameter
     * @return ItemsResponse<PushMessageSearchResponse>
     * @author Juyoung
     * @since 2024-11-05
     * @Modification 2024-11-05 Juyoung - 최초작성<br />
     * */
    @Transactional
    public GridResponse<PushMessageSearchResponse> getSearchPushMessageList(
            PushMessageSearchRequest parameter) {

        Optional<Page<L_PUSH_MSG>> lPushMsgPage = this.getMyPushMessage(parameter.pageNo(),parameter.pageSize());

        List<PushMessageSearchResponse> list = pushMessageMapper.toSearchResponseList(lPushMsgPage.get().getContent());

        return GridResponse.<PushMessageSearchResponse>builder()
                            .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                            .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                            .totalSize(lPushMsgPage.get().getTotalElements())
                            .totalPageSize(lPushMsgPage.get().getTotalPages())
                            .size(lPushMsgPage.get().getNumberOfElements())
                            .items(list)
                            .build();
    }

    private Optional<Page<L_PUSH_MSG>> getMyPushMessage(int page, int size) {
        String userId = CommonUtils.getUserId();
        // 30일 정책 미정 -> 확정되면 변경 필요
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(LIST_DISPLAY_PERIOD);
        Pageable pageable = PageRequest.of(page, size);
        return pushMessageRepository.findMyNotificationBeforeDate(userId, daysAgo, pageable);
    }

    /**
     * 링크 API
     * 링크 API를 호출 했을 때 알림 여부 'Y'로 UPDATE
     *
     * @author MINJI
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemResponse<Long> notificationActionTrigger(PushMessageCheckRequest parameter) {
        String userId = CommonUtils.getUserId();

        Optional<L_PUSH_MSG> data = pushMessageRepository.findByKey_UserIdAndKey_CreateDateAndKey_MessageTypeAndLink(userId, Converter.stringToLocalDateTime(parameter.createDate(), DateType.YYYYMMDDHHMMSS_FORMAT), parameter.messageType(), parameter.link());
        if (data.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        data.get().updateAlarmCheckYn(UseYn.Y);
        pushMessageRepository.save(data.get());

        return ItemResponse.<Long>builder()
                .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                .item(1L)
                .build();
    }


    /**
     * 테스트용 알림 메시지 추가
     *
     * @return PushMessageSaveResponse 알림 추가 결과
     * @author Minsu Son
     * @since 2024-10-30<br />
     */
    public ItemResponse<PushMessageSaveResponse> savePushMessage(
            PushMessageSaveRequest request
    ) {
        String userId = CommonUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        L_PUSH_MSG_KEY logPushMessageKey
                = L_PUSH_MSG_KEY.builder()
                .userId(userId)
                .createDate(now)
                .messageType(request.messageType())
                .build();

        M_CD_KEY masterCodeKey = M_CD_KEY.builder()
                .groupCodeId(request.groupCodeId())
                .codeId(request.codeId())
                .build();

        MSG_TYPE messageType = messageTypeRepository.findById(masterCodeKey)
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        L_PUSH_MSG logPushMessage = L_PUSH_MSG.builder()
                .key(logPushMessageKey)
                .title(request.title())
                .message(request.message())
                .link(request.link())
                .transmissionRequestDate(now)
                .transmissionDivision(TransmissionDivision.N)
                .alarmCheckYn(UseYn.N)
                .messageType(messageType)
                .build();

        pushMessageRepository.save(logPushMessage);
        PushMessageSaveResponse response
                = pushMessageMapper.toSaveResponse(logPushMessage);

        return ItemResponse.<PushMessageSaveResponse>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item(response)
                .build();
    }
}
