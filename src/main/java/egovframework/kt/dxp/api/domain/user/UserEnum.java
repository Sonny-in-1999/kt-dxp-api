package egovframework.kt.dxp.api.domain.user;

import lombok.Getter;

public class UserEnum {
    @Getter
    public enum ValidationType {
        PHONE_NUM, USER_CI;

        public static ValidationType fromString(String text) {
            if (text == null) {
                throw new IllegalArgumentException("Text cannot be null");
            }

            String normalizedText = text.toLowerCase();
            return switch (normalizedText) {
                case "phonenum", "phone_num" -> PHONE_NUM;
                case "userci", "user_ci" -> USER_CI;
                default -> throw new IllegalArgumentException(
                        "No constant with text '" + text + "' found");
            };
        }
    }
}
