package egovframework.kt.dxp.api.domain.common.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "VersionRequest - 운영체제에 따라 최신 버전의 정보를 반환")
public record VersionRequest(
        @ApiModelProperty(value = "운영체제 유형", example = "IOS/AOS")
        String operatingSystemType,
        @ApiModelProperty(value = "버전 아이디", example = "01.01.00")
        String versionId
) {
}
