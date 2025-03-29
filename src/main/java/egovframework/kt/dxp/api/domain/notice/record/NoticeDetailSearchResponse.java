package egovframework.kt.dxp.api.domain.notice.record;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 공지사항 상세 조회 응답
 *
 * @author MINJI
 * @since 2024-10-15
 */

@Schema(description = "공지사항 상세 조회 응답")
public record NoticeDetailSearchResponse(
        @ApiModelProperty(name = "공지 순번", example = "1")
        Integer noticeSequenceNumber,
        @ApiModelProperty(name = "공지 구분", example = "01")
        String noticeDivision,
        @ApiModelProperty(name = "공지 구분 명", example = "공지")
        String noticeDivisionName,
        @ApiModelProperty(name = "제목", example = "공지 입니다.")
        String title,
        @ApiModelProperty(name = "내용", example = "내용 입니다.")
        String contents,
        @ApiModelProperty(name = "생성자 아이디", example = "user1")
        String createUserId,
        @ApiModelProperty(name = "수정자 아이디", example = "modify1")
        String updateUserId,
        @ApiModelProperty(name = "생성 일시", example = "2024-10-09")
        String createDate,
        @ApiModelProperty(name = "수정 일시", example = "2024-10-09")
        String updateDate,
        @ApiModelProperty(name = "첨부파일 건수", example = "1")
        Integer fileCount,
        @ApiModelProperty(name = "첨부파일 리스트", example = "")
        List<FileSearchResponse> fileList

) {

}
