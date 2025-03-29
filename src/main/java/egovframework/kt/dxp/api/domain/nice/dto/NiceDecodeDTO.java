package egovframework.kt.dxp.api.domain.nice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceDecodeDTO
 * Description   :  
 * Creation Date : 2024-10-18
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-18, Juyoung Chae, 최초작성
 ******************************************************************************************/
@Getter
@Setter
@ApiModel("NiceDecodeDTO - Nice 안심인증 데이터 복호화 DTO")
@Deprecated
public class NiceDecodeDTO {
    @ApiModelProperty(value = "요청 번호")
    private String REQ_SEQ;
    @ApiModelProperty(value = "인증 고유번호")
    private String RES_SEQ;
    @ApiModelProperty(value = "인증 유형")
    private String AUTH_TYPE;
    @ApiModelProperty(value = "이름")
    private String NAME;
    @ApiModelProperty(value = "UTF8 인코딩 이름")
    private String UTF8_NAME;
    @ApiModelProperty(value = "생년월일", example = "YYYYMMDD")
    private String BIRTHDATE;
    @ApiModelProperty(value = "성별")
    private String GENDER;
    @ApiModelProperty(value = "내/외국인 정보")
    private String NATIONALINFO;
    @ApiModelProperty(value = "중복가입 확인값 64 byte")
    private String DI;
    @ApiModelProperty(value = "연계정보 확인값 88 byte")
    private String CI;
    @ApiModelProperty(value = "휴대폰 번호")
    private String MOBILE_NO;
    @ApiModelProperty(value = "통신사")
    private String MOBILE_CO;
}
