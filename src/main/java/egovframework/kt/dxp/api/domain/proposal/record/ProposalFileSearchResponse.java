package egovframework.kt.dxp.api.domain.proposal.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 정책 제안 조회 응답
 *
 * @author BITNA
 * @since 2024-10-29<br />
 */
@Schema(description = "정책 제안 조회 응답")
public record ProposalFileSearchResponse(
        @Schema(description = "게시판 구분", example = "01")
        String bulletinBoardDivision,
        @Schema(description = "저장 파일 명", example = "455270b2-d077-4f08-854a-7e9c668a0c81")
        String saveFileName,
        @Schema(description = "파일명", example = "")
        String fileName,
        @Schema(description = "파일 확장자", example = "")
        String fileExtension
//        ,
//        @Schema(description = "base64 image", example = "")
//        String image
) {
        @Builder
        public ProposalFileSearchResponse(String bulletinBoardDivision, String saveFileName, String fileName, String fileExtension
//                , String image
        ) {
                this.bulletinBoardDivision = bulletinBoardDivision;
                this.saveFileName = saveFileName;
                this.fileName = fileName;
                this.fileExtension = fileExtension;
//                this.image = image;
        }

}
