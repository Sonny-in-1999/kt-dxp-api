package egovframework.kt.dxp.api.domain.nice.record;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * @author GEONLEE
 * @since 2024-10-22
 * 마스킹 처리, json parsing 등을 위해 DTO 유지
 * GSON 2.9.0 이전 버전은 record parsing 안됨.
 */
@Getter
@ApiModel("Nice 안심 인증 데이터 복호화 DTO")
public class NiceDecodeDataDTO {
    @ApiModelProperty(value = "요청 번호")
    @SerializedName("REQ_SEQ")
    private String requestSequenceNumber;

    @ApiModelProperty(value = "인증 고유번호")
    @SerializedName(value = "RES_SEQ")
    private String responseSequenceNumber;

    @ApiModelProperty(value = "인증 유형")
    @SerializedName(value = "AUTH_TYPE")
    private String authType;

    @ApiModelProperty(value = "이름")
    @SerializedName(value = "NAME")
    private String name;

    @ApiModelProperty(value = "UTF8 인코딩 이름")
    @SerializedName(value = "UTF8_NAME")
    private String utf8EncodedName;

    @ApiModelProperty(value = "생년월일", example = "YYYYMMDD")
    @SerializedName(value = "BIRTHDATE")
    private String birthDate;

    @ApiModelProperty(value = "성별")
    @SerializedName(value = "GENDER")
    private String gender;

    @ApiModelProperty(value = "내/외국인 정보")
    @SerializedName(value = "NATIONALINFO")
    private String nationalInformation;

    @ApiModelProperty(value = "중복가입 확인값 64 byte")
    @SerializedName(value = "DI")
    private String di;

    @ApiModelProperty(value = "연계정보 확인값 88 byte")
    @SerializedName(value = "CI")
    private String ci;

    @ApiModelProperty(value = "휴대폰 번호")
    @SerializedName(value = "MOBILE_NO")
    private String mobilePhoneNumber;

    @ApiModelProperty(value = "통신사")
    @SerializedName(value = "MOBILE_CO")
    private String mobileCompany;

    @ApiModelProperty(value = "성인여부")
    @SerializedName(value = "IS_ADULT")
    private String isAdult;
}
