package egovframework.kt.dxp.api.domain.citizen.record;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

//검증 API 데이터
@Builder
public record MobileCitizenStartCommonRecord(
        @ApiModelProperty(name = "Type", example = "mip")
        String type,  /** 유형 */

        @ApiModelProperty(name = "Version", example = "1.0.0")
        String version,  /** 버전 */

        @ApiModelProperty(name = "Command", example = "200")
        String cmd,  /** Command */

        @ApiModelProperty(name = "Transaction Code", example = "202412190224230984F2AD31F")
        String trxcode,  /** 거래코드 */

        @ApiModelProperty(name = "Mode", example = "direct")
        String mode,  /** 모드 */

        @ApiModelProperty(name = "Profile", example = "Base64 encoded string")
        String profile,  /** Base64로 인코딩된 Profile 파싱할 의미가 없어보여 생성 안 함(profile RECORD)*/

        @ApiModelProperty(name = "Image URL", example = "https://www.mobileid.go.kr/resources/images/main/mdl_ico_homepage.ico")
        String image,  /** BI 이미지 */

        @ApiModelProperty(name = "CI", example = "false")
        boolean ci,  /** CI 포함 여부 */

        @ApiModelProperty(name = "Telephone Number", example = "false")
        boolean telno,  /** 전화번호 포함 여부 */

        @ApiModelProperty(name = "Host", example = "http://211.41.186.152:13345")
        String host  /** 호스트명 */
) {

}
