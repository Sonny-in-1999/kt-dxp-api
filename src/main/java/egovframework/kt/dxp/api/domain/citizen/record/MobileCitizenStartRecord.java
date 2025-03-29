package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

//검증 API 데이터
@Builder
public record MobileCitizenStartRecord(
        @ApiModelProperty(name = "Command", example = "")
        String cmd,          // Command
        @ApiModelProperty(name = "모드", example = "")
        String mode,         // 모드
        @ApiModelProperty(name = "서비스코드", example = "")
        String svcCode,      // 서비스코드
        @ApiModelProperty(name = "앱", example = "")
        String appCode,      // 앱
        @ApiModelProperty(name = "Base64로 인코딩된 M200 메시지", example = "")
        String m200Base64,   // Base64로 인코딩된 M200 메시지
        @ApiModelProperty(name = "Base64로 인코딩된 M120 메시지", example = "")
        String m120Base64,   // Base64로 인코딩된 M120 메시지
        @ApiModelProperty(name = "성명", example = "")
        String name,         // 성명
        @ApiModelProperty(name = "전화번호", example = "")
        String telno         // 전화번호
) {

}
