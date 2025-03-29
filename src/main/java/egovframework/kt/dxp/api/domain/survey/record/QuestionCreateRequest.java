package egovframework.kt.dxp.api.domain.survey.record;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;

public record QuestionCreateRequest(
        @ApiModelProperty(name = "질문 순번", example = "1")
        @NotNull
        Integer questionSequenceNumber,
        @ApiModelProperty(name = "항목 순번 리스트")
        List<QuestionItemCreateRequest> itemSequenceNumberList
) {

}
