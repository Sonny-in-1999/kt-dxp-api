package egovframework.kt.dxp.api.domain.user;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidatorUtil {

    @Value("${security.ccdid.salt:gbYkevtxYM6qbO24DzECrRpt1s2CdL}")
    private String salt;  // application.yml에서 설정값 주입

    /**
     * salt를 적용하여 CCDID를 SHA256으로 암호화
     */
    public String encrypt(String decryptApiKey) {
        try {
            // salt 적용
            String salted = addSalt(decryptApiKey);
            log.info("salted: {}", salted);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(salted.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 암호화 실패: {}", e.getMessage());
            throw new RuntimeException("암호화 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * CCDID에 salt 적용
     */
    private String addSalt(String ccdid) {
        return String.format("%s{%s}", ccdid, salt);
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 암호화된 CCDID 값들을 비교
     */
    public boolean validate(String input, String storedHashed) {
        // 입력받은 CCDID를 암호화 (salt 포함)
        String hashedInput = encrypt(input);

        // 상수 시간 비교를 사용하여 타이밍 공격 방지
        return MessageDigest.isEqual(
                hashedInput.getBytes(StandardCharsets.UTF_8),
                storedHashed.getBytes(StandardCharsets.UTF_8)
        );
    }
}