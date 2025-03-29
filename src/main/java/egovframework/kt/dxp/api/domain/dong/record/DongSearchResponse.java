package egovframework.kt.dxp.api.domain.dong.record;

import io.swagger.annotations.ApiModelProperty;

public record DongSearchResponse(
        @ApiModelProperty(name = "동 코드", example = "10100")
        String dongCode,
        @ApiModelProperty(name = "동 이름", example = "봉의동")
        String dongName,
        @ApiModelProperty(name = "동 생성일시", example = "2023-06-11")
        String dongCreateDate
) {

}
