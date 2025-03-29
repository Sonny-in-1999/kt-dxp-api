package egovframework.kt.dxp.api.domain.nice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : NiceCheckDTO
 * Description   :  
 * Creation Date : 2024-10-18
 * Written by    : Juyoung Chae Senior Researcher
 * History       : 1 - 2024-10-18, Juyoung Chae, 최초작성
 ******************************************************************************************/
@Getter
@Setter
@ApiModel(value = "NiceCheckDTO - Nice 안심인증 요청 파라메터 DTO, default data : authType = 'M', customize = ''")
@Deprecated
public class NiceCheckDTO {
    @ApiModelProperty(value = "인증유형 default 'M' [없으면 기본 선택화면, M(휴대폰), X(인증서공통), U(공동인증서), F(금융인증서), S(PASS인증서), C(신용카드)]", example = "M")
    private String authType = "M";
    @ApiModelProperty(value = "커스텀 default '' [없으면 기본 웹페이지 / Mobile : 모바일페이지]", example = "Mobile")
    private String customize = "";
    @ApiModelProperty(value = "성공 시 이동될 URL (인증 요청 전 페이지 도메인과 동일해야 한다.)", example = "http://www.test.co.kr/success.jsp")
    @NotNull
    private String successURL;
    @ApiModelProperty(value = "실패 시 이동될 URL (인증 요청 전 페이지 도메인과 동일해야 한다.)", example = "http://www.test.co.kr/fail.jsp")
    @NotNull
    private String errorURL;
}
