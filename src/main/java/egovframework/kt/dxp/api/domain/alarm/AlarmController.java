package egovframework.kt.dxp.api.domain.alarm;

import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.domain.alarm.record.AlarmResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "[CDA-ARA] 알림 뱃지", description = "[담당자 : Juyoung Chae]")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping(value = "/v1/alarm/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "[CDA-ARA-008] 알림 뱃지 조회", notes = "알림 뱃지를 조회합니다.")
    public ResponseEntity<ItemResponse<AlarmResponse>> getParticipationList() {
        return ResponseEntity.ok()
                             .body(alarmService.getRedBadge());
    }
}