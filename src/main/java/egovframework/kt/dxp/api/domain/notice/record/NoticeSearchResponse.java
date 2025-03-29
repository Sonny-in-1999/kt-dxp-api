package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공지사항 조회 응답
 *
 * @author MINJI
 * @apiNote 2024-10-29 BITNA setAlarmCheckYn 추가
 * @since 2024-10-15
 */
@Schema(description = "공지사항 조회 응답")
public record NoticeSearchResponse(
        @ApiModelProperty(name = "공지 순번", example = "1")
        Integer noticeSequenceNumber,
        @ApiModelProperty(name = "공지 구분", example = "01")
        String noticeDivision,
        @ApiModelProperty(name = "공지 구분 명", example = "공지")
        String noticeDivisionName,
        @ApiModelProperty(name = "제목", example = "공지 입니다.")
        String title,
        @ApiModelProperty(name = "생성 일시", example = "2024-10-09")
        String createDate,
        @ApiModelProperty(name = "경과 시간", example = "1시간 전")
        String elapsedTime,
        @ApiModelProperty(name = "알림 확인 여부")
        String alarmCheckYn,
        @ApiModelProperty(name = "파일 여부")
        String fileYn
) {
}
