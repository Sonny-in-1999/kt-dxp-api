package egovframework.kt.dxp.api.domain.common.record;


import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(value = "앱 최신 버전 정보 조회 응답")
@Builder
public record VersionResponse(
        @ApiModelProperty(value = "최신 버전 아이디", example = "02.01.20240101")
        String latestVersionId,
        @ApiModelProperty(value = "현재 버전 아이디", example = "01.00.20230101")
        String versionId,
        @ApiModelProperty(value = "현재 버전 업데이트 여부", example = "Y")
        UseYn isUpdateAvailableYn
) {
}
