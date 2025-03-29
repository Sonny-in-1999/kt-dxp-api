package egovframework.kt.dxp.api.domain.mileage;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.file.nomal.NormalFileRemover;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.config.aes.AES256;
import egovframework.kt.dxp.api.domain.mileage.record.MileageRequestUrl;
import egovframework.kt.dxp.api.domain.mileage.record.MileageSearchResponse;
import egovframework.kt.dxp.api.domain.mileage.record.MileageTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * 마일리지 Service
 *
 * @author BITNA
 * @since 2025-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class MileageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NormalFileRemover.class);

    private final MessageConfig messageConfig;
    private final WebClient.Builder webClientBuilder;

    @Value("${rc.mileage.base-url:http://210.179.57.22:8081}")
    private String baseUrl;

    @Value("${rc.mileage.auth-key:ab55c3a0c1190f387a58e4e6b213095e}")
    private String authKey;

    public ItemResponse<MileageSearchResponse> getMileage() {
        // TODO: 사용자 전화번호 가져오기

        // 올바른 JSON 형식으로 수정
        String request = "{ \"data\": [{\"mileage_type\": \"ELEC_BIKE\", \"type_key\": \"01051707708\"}, {\"mileage_type\": \"ELEC_MOTORCYCLE\", \"type_key\": \"01051707708\"}, {\"mileage_type\": \"ELEC_CAR\", \"type_key\": \"01051707708\"}] }";

        MileageTokenResponse mileageTokenResponse = getAuthToken().block();

        if (ObjectUtils.isEmpty(mileageTokenResponse.token())) {
            throw new ServiceException(ErrorCode.SERVICE_ERROR, "authToken이 없습니다.");
        }

        try {
            AES256 encoder = new AES256();
            String encryptedBody = encoder.encrypt(request, mileageTokenResponse.cipherkey());
            MileageSearchResponse response = getMileageData(mileageTokenResponse.token(), encryptedBody).block();

            if (ObjectUtils.isEmpty(response.result_code()) || !"0".equals(response.result_code())) {
                LOGGER.error("getMileageData result_code: {}, result_msg: {}", response.result_code(), response.result_msg());
                throw new ServiceException(ErrorCode.SERVICE_ERROR, "마일리지 조회에 실패하였습니다.");
            }

            return ItemResponse.<MileageSearchResponse>builder()
                    .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                    .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                    .item(response)
                    .build();

        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SERVICE_ERROR);
        }
    }

    private Mono<MileageTokenResponse> getAuthToken() {
        return webClientBuilder.baseUrl(baseUrl) // Base URL 설정
                .build()
                .get() // POST 요청
                .uri(MileageRequestUrl.AUTH_TOKEN.getUrl()) // URI 설정
                .headers(header -> {
                    header.set("Auth-Key", authKey);

                })
                .retrieve() // 응답을 받아옴
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new WebClientResponseException(clientResponse.rawStatusCode(),
                                "HTTP error: " + clientResponse.statusCode(),
                                clientResponse.headers().asHttpHeaders(), null, null)))
                .bodyToMono(MileageTokenResponse.class) // 응답을 제네릭 타입으로 변환
                .onErrorResume(e -> {
                    // 예외가 발생하면 적절히 처리 (예: 로그 기록, 기본값 반환 등)
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        return Mono.error(new RuntimeException("Error during HTTP request: " + ex.getMessage(), ex));
                    }
                    return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage(), e));
                });
    }

    private Mono<MileageSearchResponse> getMileageData(String authToken, String request) {
        return webClientBuilder.baseUrl(baseUrl) // Base URL 설정
                .build()
                .post() // POST 요청
                .uri(MileageRequestUrl.MILEAGE_SEARCH.getUrl()) // URI 설정
                .contentType(MediaType.APPLICATION_JSON)
                .headers(header -> {
                    header.set("Auth-Token", authToken);
                })
                .bodyValue(request) // 요청 body 설정
                .retrieve() // 응답을 받아옴
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new WebClientResponseException(clientResponse.rawStatusCode(),
                                "HTTP error: " + clientResponse.statusCode(),
                                clientResponse.headers().asHttpHeaders(), null, null)))
                .bodyToMono(MileageSearchResponse.class) // 응답을 제네릭 타입으로 변환
                .onErrorResume(e -> {
                    // 예외가 발생하면 적절히 처리 (예: 로그 기록, 기본값 반환 등)
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        return Mono.error(new RuntimeException("Error during HTTP request: " + ex.getMessage(), ex));
                    }
                    return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage(), e));
                });
    }

}
