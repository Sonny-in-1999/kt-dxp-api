package egovframework.kt.dxp.api.domain.main.popup.record;

import egovframework.kt.dxp.api.domain.notice.record.FileSearchResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.util.List;

public record PopupSearchResponse(
        @ApiModelProperty(name = "팝업 순번", example = "1")
        Integer popupSequenceNumber,
        @ApiModelProperty(name = "팝업 유형", example = "01")
        String popupType,
        @ApiModelProperty(name = "팝업 유형값", example = "일반")
        String popupTypeValue,
        @ApiModelProperty(name = "팝업 제목", example = "팝업 제목입니다.")
        String popupTitle,
        @ApiModelProperty(name = "링크", example = "url")
        String linkUrl,
        @ApiModelProperty(name = "첨부파일 리스트", example = "")
        List<FileSearchResponse> fileList
) {

        @Builder
        public PopupSearchResponse(
                Integer popupSequenceNumber,
                String popupType,
                String popupTypeValue,
                String popupTitle,
                String linkUrl,
                List<FileSearchResponse> fileList) {
                this.popupSequenceNumber = popupSequenceNumber;
                this.popupType = popupType;
                this.popupTypeValue = popupTypeValue;
                this.popupTitle = popupTitle;
                this.linkUrl = linkUrl;
                this.fileList = fileList;
        }
}
