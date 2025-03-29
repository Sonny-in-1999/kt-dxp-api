package egovframework.kt.dxp.api.domain.welfare.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Nullable;
import java.util.List;

@Schema(name = "복지 정책 상세 응답")
public record WelfareDetailSearchResponse(

        @Schema(name = "게시글 제목", example = "고용촉진장려금 지원 사업")
        String title,

        @Schema(name = "등록일자", example = "2024.10.12")
        String createDate,

        @Schema(name = "복지 정책 분류명", example = "저소득 지원")
        String welfareDivisionName,

        @Schema(name = "복지 정책 세부 분류명", example = "청소년")
        @Nullable
        String welfareDetailDivisionName,

        @Schema(name = "게시글 내용", example = "게시글 내용입니다.")
        String contents,

//        @Schema(name = "대표 이미지", example = "대표 이미지 입니다.")
//        String mainImage,

        @Schema(name = "복지 정책 조회 수", example = "55")
        int searchCount,

        @Schema(name = "생성자 아이디", example = "test1")
        String createUserId,

        @Schema(name = "첨부파일 건수", example = "3")
        int fileCount,

        @Schema(name = "파일 정보", example = "파일 정보 입니다.")
        List<FileSearchResponse> fileList,

        @ApiModelProperty(name = "대표 이미지", example = "대표 이미지 입니다.")
        List<FileSearchResponse> mainImageFile,

        @Schema(name = "버튼 목록", example = "버튼 목록 입니다.")
        List<WelfareButtonSearchResponse> buttonList
) {
}
