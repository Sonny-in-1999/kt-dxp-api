package egovframework.kt.dxp.api.domain.nice.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@Builder
public record NiceDecodeResponse(
        @ApiModelProperty(value = "Nice 로 부터 전달받은 응답 코드", example = "OK")
        String code,
        @ApiModelProperty(value = "응답 메시지", example = "데이터를 복호화 하였습니다.")
        String message,
        @ApiModelProperty(value = "Nice 로 부터 전달받은 디코드된 데이터")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        NiceDecodeDataDTO decodedData,

        @ApiModelProperty(value = "Decode Response Data")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        NiceDecodeResponseData responseData
) {
        @Builder
        public record NiceDecodeResponseData(
                @ApiModelProperty(value = "성인여부")
                @SerializedName(value = "IS_ADULT")
                String isAdult
        ){
        }
}
