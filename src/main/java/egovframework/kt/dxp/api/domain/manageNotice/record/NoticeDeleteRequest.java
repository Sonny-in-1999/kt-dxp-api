package egovframework.kt.dxp.api.domain.manageNotice.record;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.annotations.ApiModelProperty;

public record NoticeDeleteRequest(
        @ApiModelProperty(name = "공지 순번", example = "1")
        @NotNull
        Integer noticeSequenceNumber
) {

}
