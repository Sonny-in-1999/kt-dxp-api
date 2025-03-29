package egovframework.kt.dxp.api.domain.welfare.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Nullable;
import java.util.List;

@Schema(description = "복지 정책 목록 응답")
public record WelfareListSearchResponse(

        @Schema(name = "복지 순번", example = "1")
        Integer welfareSequenceNumber,

        @Schema(name = "게시글 제목", example = "2024년 고용촉진장려금 지원 사업")
        String title,

        @Schema(name = "등록일자", example = "2024.10.12")
        String createDate,

        @Schema(name = "복지 정책 분류명", example = "저소득 지원")
        String welfareDivisionName,

        @Schema(name = "복지 정책 세부 분류명", example = "청소년")
        @Nullable
        String welfareDetailDivisionName,

//        @Schema(name = "대표 이미지", example = "대표 이미지 입니다.")
//        String mainImage,
        @ApiModelProperty(name = "대표 이미지", example = "대표 이미지 입니다.")
        List<FileSearchResponse> mainImageFile,

        @Schema(name = "복지 정책 조회 수", example = "55")
        int searchCount,

        @Schema(name = "생성자 아이디", example = "test1")
        String createUserId
) {
}
