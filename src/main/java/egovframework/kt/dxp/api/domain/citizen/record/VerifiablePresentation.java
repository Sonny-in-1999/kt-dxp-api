package egovframework.kt.dxp.api.domain.citizen.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
/**
 * 검증 가능한 제시(VP) 데이터 구조
 */
public record VerifiablePresentation(
        @Schema(description = "컨텍스트")
        @JsonProperty("@context")
        List<String> context,

        @Schema(description = "만료 일시")
        String expirationDate,

        @Schema(description = "식별자")
        String id,

        @Schema(description = "증명 정보")
        Proof proof,

        @Schema(description = "타입")
        List<String> type,

        @Schema(description = "미확정 데이터")
        Uncommitted uncommitted,

        @Schema(description = "검증 가능한 자격 증명 목록")
        List<VerifiableCredential> verifiableCredential
) {
    /**
     * 증명 정보
     */
    public record Proof(
            @Schema(description = "생성 일시")
            String created,

            @Schema(description = "생성자")
            String creator,

            @Schema(description = "난수값, CI")
            String nonce,

            @Schema(description = "서명값")
            String signatureValue,

            @Schema(description = "증명 타입")
            String type
    ) {}

    /**
     * 미확정 데이터 정보
     */
    public record Uncommitted(
            @Schema(description = "인증기관 정보")
            CaInfo caInfo,

            @Schema(description = "미확정 서명")
            String signUncommitted
    ) {
        /**
         * 인증기관 정보
         */
        public record CaInfo(
                @Schema(description = "인증서")
                String attestationCert,

                @Schema(description = "인증기관 앱 ID")
                String caAppId,

                @Schema(description = "얼굴인증 토큰")
                String faceAccessToken,

                @Schema(description = "얼굴인증 타입")
                String faceType,

                @Schema(description = "패키지명")
                String packageName,

                @Schema(description = "VP 식별자")
                String vpId,

                @Schema(description = "지갑 인증번호")
                String walletAuthNo,

                @Schema(description = "지갑 ID")
                String walletId
        ) {}
    }

    /**
     * 검증 가능한 자격 증명(VC)
     */
    public record VerifiableCredential(
            @Schema(description = "컨텍스트")
            @JsonProperty("@context")
            List<String> context,

            @Schema(description = "주장 정보")
            Assertion assertion,

            @Schema(description = "자격 증명 주체")
            CredentialSubject credentialSubject,

            @Schema(description = "인코딩")
            String encoding,

            @Schema(description = "증거 목록")
            List<Evidence> evidence,

            @Schema(description = "만료 일시")
            String expirationDate,

            @Schema(description = "식별자")
            String id,

            @Schema(description = "발급 일시")
            String issuanceDate,

            @Schema(description = "발급자 정보")
            Issuer issuer,

            @Schema(description = "언어")
            String language,

            @Schema(description = "부모 식별자")
            String parentId,

            @Schema(description = "증명 정보")
            Proof proof,

            @Schema(description = "서명 키 ID")
            String signKeyId,

            @Schema(description = "시간대")
            String timezone,

            @Schema(description = "타입")
            List<String> type,

            @Schema(description = "VC 정의")
            String vcDef,

            @Schema(description = "버전")
            String version
    ) {
        /**
         * 주장 정보
         */
        public record Assertion(
                @Schema(description = "코드")
                String code,

                @Schema(description = "설명")
                String desc,

                @Schema(description = "이름")
                String name
        ) {}

        /**
         * 자격 증명 주체
         */
        public record CredentialSubject(
                @Schema(description = "식별자")
                String id,

                @Schema(description = "개인정보 목록")
                List<Privacy> privacy
        ) {
            /**
             * 개인정보
             */
            public record Privacy(
                    @Schema(description = "타입")
                    String type,

                    @Schema(description = "값")
                    String value
            ) {}
        }

        /**
         * 증거 정보
         */
        public record Evidence(
                @Schema(description = "문서 제시 여부")
                String documentPresence,

                @Schema(description = "증거 문서")
                String evidenceDocument,

                @Schema(description = "주체 제시 여부")
                String subjectPresence,

                @Schema(description = "타입")
                String type,

                @Schema(description = "검증자")
                String verifier
        ) {}

        /**
         * 발급자 정보
         */
        public record Issuer(
                @Schema(description = "설명")
                String desc,

                @Schema(description = "식별자")
                String id,

                @Schema(description = "이름")
                String name
        ) {}
    }
}