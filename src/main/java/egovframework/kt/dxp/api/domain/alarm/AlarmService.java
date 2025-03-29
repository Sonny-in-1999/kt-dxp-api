package egovframework.kt.dxp.api.domain.alarm;

import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.alarm.record.AlarmResponse;
import egovframework.kt.dxp.api.domain.notice.NoticeRepository;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageRepository;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "[CDA-ARA] 알림 뱃지", description = "[담당자 : Juyoung Chae]")
@RequiredArgsConstructor
public class AlarmService {
    private final MessageConfig messageConfig;
    private final PushMessageRepository pushMessageRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public ItemResponse<AlarmResponse> getRedBadge() {
        String userId = CommonUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        UseYn hasRedBadgeNotice;
        UseYn hasRedBadgePushMessage = UseYn.N;

        hasRedBadgeNotice = noticeRepository.noticeCheckYn(userId, now);
        Boolean isRedBadgePushMessage = pushMessageRepository.existsByKeyUserIdAndAlarmCheckYnAndTransmissionDivisionAndKeyCreateDateLessThanEqualAndKeyCreateDateAfter(
                userId,
                UseYn.N,
                TransmissionDivision.Y,
                now, now.minusDays(30));

        if (Boolean.TRUE.equals(isRedBadgePushMessage)) {
            hasRedBadgePushMessage = UseYn.Y;
        }

        AlarmResponse alarmResponse = AlarmResponse.builder()
                                                   .hasRedBadgeNotice(hasRedBadgeNotice)
                                                   .hasRedBadgePushMessage(hasRedBadgePushMessage)
                                                   .build();
        return ItemResponse.<AlarmResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(alarmResponse)
                .build();
    }
}

