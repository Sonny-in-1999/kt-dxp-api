package egovframework.kt.dxp.api.domain.nice.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * @author GEONLEE
 * @since 2024-10-22
 */
@ApiModel(value = "NiceCheckDTO - Nice 안심인증 요청 정보, default data : authType = 'M', customize = ''")
public record NiceCheckRequest(
        @ApiModelProperty(value = "인증유형 default 'M' [없으면 기본 선택화면, M(휴대폰), X(인증서공통), U(공동인증서), F(금융인증서), S(PASS인증서), C(신용카드)]", example = "M")
        String authType,
        @ApiModelProperty(value = "커스텀 default '' [없으면 기본 웹페이지 / Mobile : 모바일페이지]", example = "Mobile")
        String customize,
        @ApiModelProperty(value = "성공 시 이동될 URL (인증 요청 전 페이지 도메인과 동일해야 한다.)", example = "http://www.test.co.kr/success.jsp")
        @NotNull
        String successURL,
        @ApiModelProperty(value = "실패 시 이동될 URL (인증 요청 전 페이지 도메인과 동일해야 한다.)", example = "http://www.test.co.kr/fail.jsp")
        @NotNull
        String errorURL
) {
        @Builder
        public NiceCheckRequest(String authType, String customize, String successURL, String errorURL) {
                this.authType = (StringUtils.isEmpty(authType)) ? "M" : authType;
                this.customize = (StringUtils.isEmpty(customize)) ? "" : customize;
                this.successURL = successURL;
                this.errorURL = errorURL;
        }
}
