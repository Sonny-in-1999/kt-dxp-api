package egovframework.kt.dxp.api.domain.accountManagement.record;

import io.swagger.v3.oas.annotations.media.Schema;

/*******************************************************************************************
 * Project       : Chuncheon DID Project
 * Module        : kt-dxp-api
 * Filename      : AccountInformationSearch Response
 * Description   :  계정정보 관리 조회 응답
 * Creation Date : 2024-10-28
 * Written by    : BITNA
 * History       : 1 - 2024-10-28, BITNA, 최초작성
 ******************************************************************************************/
@Schema(name = "계정정보 조회 응답")
public record AccountInformationSearchResponse(
        @Schema(description = "이름", example = "홍길동")
        String userName,
        @Schema(description = "휴대폰 번호", example = "010-12**-56**")
        String mobilePhoneNumber,
        @Schema(description = "생년월일", example = "2024.10.23")
        String birthDate
) {

}
