package egovframework.kt.dxp.api.domain.user;

import io.swagger.annotations.ApiModelProperty;

public record UserValidateEncodedRequest(
        @ApiModelProperty(name = "Base64 인코딩된 요청 데이터", example = "cGhvbmVOdW18MDEwLTEyMzQtNTY3OA==", notes = "Base64로 인코딩된 Json 문자열")
        String encodedData
) {
}
