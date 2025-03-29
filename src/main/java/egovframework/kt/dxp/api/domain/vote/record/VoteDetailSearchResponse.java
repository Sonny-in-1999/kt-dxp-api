package egovframework.kt.dxp.api.domain.vote.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;


/**
 * 투표 조회 요청
 *
 * @author BITNA
 * @apiNote 2024-11-06 BITNA selectLimitCount -> min/maxSelectCount 컬럼변경
 * 2024-11-20 BITNA 상세조회 시 image 반환 추가
 * @since 2024-10-31<br />
 */
@Builder
@Schema(description = "투표 조회 요청")
public record VoteDetailSearchResponse(
        @Schema(description = "투표 순번 / int(11)", example = "1")
        Integer voteSequenceNumber,
        @Schema(description = "제목 / varchar(100)", example = "")
        String title,
        @Schema(description = "최소 선택 제한수", example = "")
        Integer minSelectCount,
        @Schema(description = "최대 선택 제한수", example = "")
        Integer maxSelectCount,
        @Schema(description = "내용 / text", example = "")
        String contents,
        @Schema(description = "참여 시작 연령 / int(2)", example = "1")
        Integer participationStartAge,
        @Schema(description = "참여 종료 연령 / int(2)", example = "1")
        Integer participationEndAge,
        @Schema(description = "참여가능 연령 설명/ String", example = "모든 연령/10세~20세")
        String ageDescription,
        @Schema(description = "남성 가능 여부 / varchar(1)", example = "")
        UseYn maleAbleYn,
        @Schema(description = "여성 가능 여부 / varchar(1)", example = "")
        UseYn femaleAbleYn,
        @Schema(description = "시작 일시 / datetime", example = "")
        String startDate,
        @Schema(description = "종료 일시 / datetime", example = "")
        String endDate,
        @Schema(description = "종료 여부 / varchar(1)", example = "")
        UseYn endYn,
        @Schema(description = "남은 시간 / varchar(36)", example = "")
        String limitTime,
        @Schema(description = "투표 구분(투표가능[01], 투표불가능[02], 투표완료[03]) / varchar(36)", example = "")
        String voteDiv,
        @Schema(description = "참여중 인원 / varchar(36)", example = "")
        Integer voteCount,
        @ApiModelProperty(name = "첨부파일 리스트", example = "")
        List<FileSearchResponse> fileList,
        @Schema(description = "항목 리스트 / varchar(36)", example = "")
        List<VoteItemSearchResponse> itemList) {
}
