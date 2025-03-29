package egovframework.kt.dxp.api.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

public record UserValidateRequest(
        @ApiModelProperty(name = "인증 유형", example = "PHONE_NUM", notes = "PHONE_NUM 또는 USER_CI")
        String validationType,
        @ApiModelProperty(name = "인증 데이터", example = "01012345678", notes = "전화번호 또는 CI 값")
        String value
) {
        /**
         * Base64 인코딩된 문자열을 디코딩하여 UserValidateRequest 객체 생성
         */
        public static UserValidateRequest fromEncodedString(String encodedData) {
                try {
                        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
                        String decodedString = new String(decodedBytes);

                        // JSON 형식으로 디코딩된 문자열을 객체로 변환
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(decodedString, UserValidateRequest.class);

                } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid encoded data format", e);
                }
        }
}
