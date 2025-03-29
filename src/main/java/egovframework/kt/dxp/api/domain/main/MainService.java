package egovframework.kt.dxp.api.domain.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.IdentityCode;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.ServiceEndpoint;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.Status;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenMasterRepository;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenTransactionRepository;
import egovframework.kt.dxp.api.domain.citizen.record.CredentialData;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.interestMenu.InterestMenuRepository;
import egovframework.kt.dxp.api.domain.main.popup.PopupMapper;
import egovframework.kt.dxp.api.domain.main.popup.PopupRepository;
import egovframework.kt.dxp.api.domain.main.popup.record.PopupCheckRequest;
import egovframework.kt.dxp.api.domain.main.popup.record.PopupSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.BannerSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.MainSearchResponse;
import egovframework.kt.dxp.api.domain.main.record.PopupSearchRequest;
import egovframework.kt.dxp.api.domain.notice.NoticeRepository;
import egovframework.kt.dxp.api.domain.notice.NoticeUserRepository;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.M_BANNER;
import egovframework.kt.dxp.api.entity.M_MOBILE_ID;
import egovframework.kt.dxp.api.entity.M_POPUP;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.M_USR_MENU;
import egovframework.kt.dxp.api.entity.TB_TRX_INFO;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class MainService {

    @Value("${sp.did.service-code:chuncheon.1}")
    private String serviceCode;

    @Value("${sp.did.base-url:http://211.41.186.152:13345}")
    private String baseUrl;

    private static final String POPUP_BBS_DIV = "04";
    private static final String BANNER_BBS_DIV = "07";
    private final Builder webClientBuilder;
    private final InterestMenuRepository interestmenuRepository;
    private final BannerRepository bannerRepository;
    private final PushMessageRepository pushMessageRepository;
    private final UserRepository userRepository;
    private final MobileCitizenMasterRepository masterRepository;
    private final MobileCitizenTransactionRepository mobileCitizenTransactionRepository;
    private final NoticeUserRepository noticeUserRepository;
    private final NoticeRepository noticeRepository;
    private final PopupRepository popupRepository;
    private final FileRepository fileRepository;
    private static final MainMapper mainMapper = MainMapper.INSTANCE;
    private static final PopupMapper popupMapper = PopupMapper.INSTANCE;
    private static final FileMapper fileMapper = FileMapper.INSTANCE;
    private final MessageConfig messageConfig;
    private final ObjectMapper objectMapper;

    @Transactional
    public ItemResponse<MainSearchResponse> getSearchMainList() throws Exception {
        ServiceEndpoint serviceEndpoint = ServiceEndpoint.MIP_VPDATA;
        List<CredentialData> credentialDataList = new ArrayList<>();
        CredentialData credentialData = null;

        LocalDateTime currentDateTime = LocalDateTime.now();
        String userId = CommonUtils.getUserId();
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));

        Optional<TB_TRX_INFO> tbTrxInfo = Optional.empty();
        // TODO DID 추가하기
        //M_USR mUsr = userRepository.findById(userId)
        //                           .orElseThrow(() -> new ServiceException(
        //                                   ErrorCode.NO_DATA, userId + " doesn't exist."));

        List<String> statusList = Arrays.asList(Status.APPROVED.name(), Status.EXPIRED.name());

        List<M_MOBILE_ID> mobileIds = masterRepository.findByKeyUserIdAndIssuedStatusCodeInOrderByBookmarkYnDescDisplaySequenceDescCreateDateDesc(
                userId, statusList);
        if(!mobileIds.isEmpty()) {
            //TODO: mobileIds 리스트 사이즈 만큼 반복하면서 credentialData에 값을 세팅
            for (M_MOBILE_ID mobileId : mobileIds) {
                credentialData = CredentialData.builder()
                                               .trxCode(mobileId.getKey().getTrxCode())
                                               .expirationDate(mobileId.getValidDate() != null
                                                       ? Converter.localDateTimeToString(
                                                       mobileId.getValidDate(),
                                                       DateType.YYYYMMDD_FORMAT) : null)
                                               .issuanceDate(mobileId.getApprovalDate() != null
                                                       ? Converter.localDateTimeToString(
                                                       mobileId.getApprovalDate(),
                                                       DateType.YYYYMMDD_FORMAT) : null)
                                               .issuerName(IdentityCode.fromCode(
                                                       mobileId.getIdentityCode()).getDescription())
                                               .issuerCode(mobileId.getIdentityCode())
                                               .isChuncheonAddress(mobileId.getChuncheonYn())
                                               .bookmarkYn(mobileId.getBookmarkYn())
                                               .name(mUsr.getUserName())
                                               .address(mobileId.getAddress())
                                               .birth(mUsr.getBirthDate())
                                               .validDateYn(Converter.getRemainingValidDate(currentDateTime, mobileId.getValidDate()))
                                               .build();

                credentialDataList.add(credentialData);
            }
        }
//        if (mUsr.getTrxCode() != null) {
//            tbTrxInfo = mobileCitizenTransactionRepository.findById(mUsr.getTrxCode());
//            // tbTrxInfo가 null이 아닌 경우 처리
//        }
//        //Optional<TB_TRX_INFO> tbTrxInfo = mobileCitizenRepository.findById(mUsr.getTrxCode());
//
//        if (tbTrxInfo.isPresent()) {
////log.info("tbTrxInfo.isPresent() in??");
//            if (UseYn.Y.equals(tbTrxInfo.get().getVpVerifyResult())) {
//                //log.info("tbTrxInfo.getVpVerifyResult() in??");
//                //VP 복호화 요청
//                //MobileCitizenVpBase64Request mobileCitizenVpBase64Request = MobileCitizenVpBase64Request.builder().vp(tbTrxInfo.get().getVp()).build();
//
//
//                // ObjectMapper 생성
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                // VP JSON 생성
//                ObjectNode vpNode = objectMapper.createObjectNode();
//                vpNode.put("vp", tbTrxInfo.get().getVp());
//                // JsonNode를 String으로 변환
//                String vpJsonString = objectMapper.writeValueAsString(vpNode);
//                log.info("base64: {}", encodeToBase64(String.valueOf(vpJsonString)));
//                MobileCitizenVpDataRequest mobileCitizenVpDataRequest = MobileCitizenVpDataRequest.builder()
//                                                                                                  .data(encodeToBase64(String.valueOf(vpJsonString)))
//                                                                                                  .build();
//
//                Mono<MobileCitizenResponse> response = sendPostRequest(baseUrl,
//                        mobileCitizenVpDataRequest, serviceEndpoint, MobileCitizenResponse.class);
//                MobileCitizenResponse mobileCitizenResponse = response.block();
//                JsonNode jsonNode = getJson(mobileCitizenResponse.data());
//                Map<String, Object> extractedData = extractCredentialData(jsonNode);
//                credentialData = CredentialData.builder()
//                                                              .expirationDate((String) extractedData.get("expirationDate"))
//                                                              .issuanceDate((String) extractedData.get("issuanceDate"))
//                                                              .issuerName((String) extractedData.get("issuerName"))
//                                                              .name((String) extractedData.get("name"))
//                                                              .address((String) extractedData.get("address"))
//                                                              .birth((String) extractedData.get("birth"))
//                                                              .build();
//                credentialDataList.add(credentialData);
//            }
//        }

        UseYn pushMessage = CommonUtils.checkPushMessageYn(userId, pushMessageRepository,
                noticeRepository);
        List<M_USR_MENU> menuList = interestmenuRepository.findByKeyUserIdAndMenuUseYnIsOrderBySortSequenceNumberAsc(
                userId, UseYn.Y);

        List<M_BANNER> bannerList = bannerRepository
                .findTop9ByDisplayYnOrderBySortSequenceNumberAsc(UseYn.Y);

        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumberIn(
                BANNER_BBS_DIV,
                bannerList.stream().map(M_BANNER::getBannerSequenceNumber).toList());

        List<BannerSearchResponse> bannerSearchResponseList = bannerList.stream()
                                                                        .map(bannerEntity -> BannerSearchResponse.builder()
                                                                                                                 .bannerSequenceNumber(
                                                                                                                         bannerEntity.getBannerSequenceNumber())
                                                                                                                 .title(bannerEntity.getTitle())
                                                                                                                 .linkUniformResourceLocator(
                                                                                                                         bannerEntity.getLinkUniformResourceLocator())
                                                                                                                 .fileList(
                                                                                                                         fileMapper.toSearchResponseList(
                                                                                                                                 fileList.stream()
                                                                                                                                         .filter(file -> file.getBulletinBoardSequenceNumber()
                                                                                                                                                             .equals(bannerEntity.getBannerSequenceNumber()))
                                                                                                                                         .toList()))
                                                                                                                 .build())
                                                                        .toList();
        MainSearchResponse mainSearchResponse = new MainSearchResponse(
                credentialDataList,
                pushMessage,
                mainMapper.toMenuSearchResponseList(menuList),
                bannerSearchResponseList);

        return ItemResponse.<MainSearchResponse>builder()
                           .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                           .item(mainSearchResponse)
                           .build();
    }

    public ItemsResponse<PopupSearchResponse> getSearchPopupList(PopupSearchRequest request) {

        List<M_POPUP> list =
                (request.popupSequenceNumber() == null || request.popupSequenceNumber().isEmpty())
                        ? popupRepository.findByDisplayYnOrderBySortSequenceNumberAsc(UseYn.Y)
                        : popupRepository.findByDisplayYnAndPopupSequenceNumberNotInOrderBySortSequenceNumberAsc(
                                UseYn.Y, request.popupSequenceNumber());

        List<L_FILE> fileList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumberIn(
                POPUP_BBS_DIV, list.stream().map(M_POPUP::getPopupSequenceNumber).toList());

        List<PopupSearchResponse> responseList = list.stream()
                                                     .map(popupEntity -> PopupSearchResponse.builder()
                                                                                            .popupSequenceNumber(
                                                                                                    popupEntity.getPopupSequenceNumber())
                                                                                            .popupType(
                                                                                                    popupEntity.getPopupType())
                                                                                            .popupTypeValue(
                                                                                                    popupEntity.getPopupTypeCode()
                                                                                                               .getPopupTypeCodeValue())
                                                                                            .popupTitle(
                                                                                                    popupEntity.getPopupTitle())
                                                                                            .linkUrl(
                                                                                                    popupEntity.getLinkUrl())
                                                                                            .fileList(
                                                                                                    fileMapper.toSearchResponseList(
                                                                                                            fileList.stream()
                                                                                                                    .filter(file -> file.getBulletinBoardSequenceNumber()
                                                                                                                                        .equals(popupEntity.getPopupSequenceNumber()))
                                                                                                                    .toList()))
                                                                                            .build())
                                                     .toList();

        return ItemsResponse.<PopupSearchResponse>builder()
                            .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                            .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                            .items(responseList)
                            .build();
    }

    public ItemResponse<Boolean> checkPopupView(PopupCheckRequest request) {

        Optional<M_POPUP> popupOptional = popupRepository.findByPopupSequenceNumberAndPopupType(request.popupSequenceNumber(),request.popupType());
        if (popupOptional.isEmpty()) {
            return ItemResponse.<Boolean>builder()
                                .status(messageConfig.getCode(ErrorCode.NO_DATA))
                                .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                                .item(false)
                                .build();
        }
        popupOptional.get().updateClickCnt(popupOptional.get().getClickCnt() + 1);
        popupRepository.save(popupOptional.get());
        return ItemResponse.<Boolean>builder()
                            .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                            .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                            .item(true)
                            .build();
    }


    // WebClient를 사용하여 POST 요청을 보내는 제네릭 함수 (예외 처리 추가)
    public <T> Mono<T> sendPostRequest(String baseUrl, Object request,
            ServiceEndpoint serviceEndpoint, Class<T> clazz) {
        return webClientBuilder.baseUrl(baseUrl) // Base URL 설정
                               .build()
                               .post() // POST 요청
                               .uri(serviceEndpoint.getUrl()) // URI 설정
                               .bodyValue(request) // 요청 body 설정
                               .retrieve() // 응답을 받아옴
                               .onStatus(status -> status.is4xxClientError()
                                               || status.is5xxServerError(),
                                       clientResponse -> Mono.error(new WebClientResponseException(
                                               clientResponse.rawStatusCode(),
                                               "HTTP error: " + clientResponse.statusCode(),
                                               clientResponse.headers().asHttpHeaders(), null,
                                               null)))
                               .bodyToMono(clazz) // 응답을 제네릭 타입으로 변환
                               .onErrorResume(e -> {
                                   // 예외가 발생하면 적절히 처리 (예: 로그 기록, 기본값 반환 등)
                                   if (e instanceof WebClientResponseException) {
                                       WebClientResponseException ex = (WebClientResponseException) e;
                                       return Mono.error(new RuntimeException(
                                               "Error during HTTP request: " + ex.getMessage(),
                                               ex));
                                   }
                                   return Mono.error(new RuntimeException(
                                           "Unexpected error: " + e.getMessage(), e));
                               });
    }

    public JsonNode getJson(String base64Data) throws Exception {
        if (base64Data == null || base64Data.isEmpty()) {
            throw new IllegalArgumentException("Base64 data must not be null or empty.");
        }
        try {
            // URL-Safe Base64 디코딩 시도
            byte[] decodedBytes;
            if (base64Data.contains("-") || base64Data.contains("_")) {
                // URL-Safe Base64 처리
                decodedBytes = Base64.getUrlDecoder().decode(base64Data);
            } else {
                // 일반 Base64 처리
                decodedBytes = Base64.getDecoder().decode(base64Data);
            }

            // 디코딩된 바이트 배열을 JSON 문자열로 변환
            String jsonString = new String(decodedBytes);

            // JSON 문자열을 JsonNode로 변환
            return objectMapper.readTree(jsonString);
        } catch (IllegalArgumentException e) {
            // Base64 형식 오류 처리
            throw new Exception("Invalid Base64 data provided.", e);
        } catch (Exception e) {
            // JSON 변환 오류 처리
            throw new Exception("Error occurred while parsing Base64 data to JSON.", e);
        }
    }

    // Base64 인코딩 함수
    public static String encodeToBase64(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        return Base64Utils.encodeToUrlSafeString(input.getBytes());
    }

    public static Map<String, Object> extractCredentialData(String jsonData) throws IOException {
        // Jackson ObjectMapper 초기화
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);

        // 결과 데이터를 담을 Map
        Map<String, Object> extractedData = new HashMap<>();

        // expirationDate, issuanceDate, issuer의 name 추출
        JsonNode verifiableCredentialNode = root.get("verifiableCredential");
        if (verifiableCredentialNode != null && verifiableCredentialNode.isArray()) {
            for (JsonNode credential : verifiableCredentialNode) {
                // expirationDate, issuanceDate
                String expirationDate = credential.path("expirationDate").asText();
                String issuanceDate = credential.path("issuanceDate").asText();

                // issuer.name
                String issuerName = credential.path("issuer").path("name").asText();

                // credentialSubject의 type과 value 추출
                JsonNode privacyNode = credential.path("credentialSubject").path("privacy");
                if (privacyNode != null && privacyNode.isArray()) {
                    for (JsonNode item : privacyNode) {
                        String type = item.path("type").asText();
                        String value = item.path("value").asText();
                        extractedData.put(type, value); // type을 키로, value를 값으로 추가
                    }
                }

                // expirationDate, issuanceDate, issuerName 저장
                extractedData.put("expirationDate", expirationDate);
                extractedData.put("issuanceDate", issuanceDate);
                extractedData.put("issuerName", issuerName);
            }
        }

        return extractedData;
    }

    public static Map<String, Object> extractCredentialData(JsonNode root)
            throws JsonProcessingException {
        // 결과 데이터를 담을 Map
        Map<String, Object> extractedData = new HashMap<>();

        //String jsonString = """
        //        {
        //          "@context": [
        //            "https://www.w3.org/2018/credentials/v1"
        //          ],
        //          "expirationDate": "2024-12-20T05:14:43",
        //          "id": "EA376C5A-A184-4F90-A607-88EB779C43B6",
        //          "proof": {
        //            "created": "2024-12-19T17:14:43",
        //            "creator": "did:kr:mobileid:3eF1sEPSEoL1r4MPtoEQsXDPRSKo#BIO1",
        //            "nonce": "2f488a6a1959f3ba7d25224c85111c33f0c34bab40729e53c6731ba2afd37dc0",
        //            "signatureValue": "3nACM5KXFP3tCKa8vr7X8QyB78wJQeNJLHfbBzhbzum7Zs11xpjqJwvXQ8KNp1fhhiuA4cztPiSphsGWETXis73nZ",
        //            "type": "Secp256r1VerificationKey2018"
        //          },
        //          "type": [
        //            "VerifiablePresentation"
        //          ],
        //          "uncommitted": {
        //            "caInfo": {
        //              "attestationCert": "",
        //              "caAppId": "",
        //              "faceAccessToken": "",
        //              "faceType": "",
        //              "packageName": "kr.go.mobileid.tbe",
        //              "vpId": "EA376C5A-A184-4F90-A607-88EB779C43B6",
        //              "walletAuthNo": "",
        //              "walletId": ""
        //            },
        //            "signUncommitted": ""
        //          },
        //          "verifiableCredential": [
        //            {
        //              "@context": [
        //                "https://www.w3.org/2018/credentials/v1"
        //              ],
        //              "assertion": {
        //                "code": "devtestmdl2",
        //                "desc": "로컬개발테스트",
        //                "name": "로컬개발테스트"
        //              },
        //              "credentialSubject": {
        //                "id": "did:kr:mobileid:3eF1sEPSEoL1r4MPtoEQsXDPRSKo",
        //                "privacy": [
        //                  {
        //                    "type": "address",
        //                    "value": "서울특별시 광진구 긴고랑로 26길 6 302호"
        //                  }
        //                ]
        //              },
        //              "encoding": "UTF-8",
        //              "evidence": [
        //                {
        //                  "documentPresence": "Physical",
        //                  "evidenceDocument": "4",
        //                  "subjectPresence": "Physical",
        //                  "type": "DocumentVerification",
        //                  "verifier": "Issuer"
        //                }
        //              ],
        //              "expirationDate": "2027-12-19T23:59:59",
        //              "id": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "issuanceDate": "2024-12-19T10:48:08",
        //              "issuer": {
        //                "desc": "",
        //                "id": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3",
        //                "name": "운전면허증-테스트베드DEV"
        //              },
        //              "language": "KR",
        //              "parentId": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "proof": {
        //                "created": "2024-12-19T10:48:08",
        //                "creator": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3#tss.devmdl01",
        //                "signatureValue": "SIG_R1_KkVBAhkdwAGjBeH3Em5vdN5nozA5AkDQd35aAApYkQe55VKvNTx8fgm4AsNQXiLWGAX7DE6XrXgzk8iF4zX7vk28X8dSMQ",
        //                "type": "Secp256r1VerificationKey2018"
        //              },
        //              "signKeyId": "tss.devmdl01",
        //              "timezone": "UTC+9",
        //              "type": [
        //                "VerifiableCredential",
        //                "OmniOneCredential"
        //              ],
        //              "vcDef": "tsdevmdl.vc",
        //              "version": "3.0"
        //            },
        //            {
        //              "@context": [
        //                "https://www.w3.org/2018/credentials/v1"
        //              ],
        //              "assertion": {
        //                "code": "devtestmdl2",
        //                "desc": "로컬개발테스트",
        //                "name": "로컬개발테스트"
        //              },
        //              "credentialSubject": {
        //                "id": "did:kr:mobileid:3eF1sEPSEoL1r4MPtoEQsXDPRSKo",
        //                "privacy": [
        //                  {
        //                    "type": "birth",
        //                    "value": "19980425"
        //                  }
        //                ]
        //              },
        //              "encoding": "UTF-8",
        //              "evidence": [
        //                {
        //                  "documentPresence": "Physical",
        //                  "evidenceDocument": "4",
        //                  "subjectPresence": "Physical",
        //                  "type": "DocumentVerification",
        //                  "verifier": "Issuer"
        //                }
        //              ],
        //              "expirationDate": "2027-12-19T23:59:59",
        //              "id": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "issuanceDate": "2024-12-19T10:48:08",
        //              "issuer": {
        //                "desc": "",
        //                "id": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3",
        //                "name": "운전면허증-테스트베드DEV"
        //              },
        //              "language": "KR",
        //              "parentId": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "proof": {
        //                "created": "2024-12-19T10:48:08",
        //                "creator": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3#tss.devmdl01",
        //                "signatureValue": "SIG_R1_KH2GCgPujjRqViPSLTzDnqHMaHPQe1mnYKAownpLrPsJ3EAmwL4gdDwoFS9BeyyfTQrdponQDCuVRUocuR6m3gzzjuXpG7",
        //                "type": "Secp256r1VerificationKey2018"
        //              },
        //              "signKeyId": "tss.devmdl01",
        //              "timezone": "UTC+9",
        //              "type": [
        //                "VerifiableCredential",
        //                "OmniOneCredential"
        //              ],
        //              "vcDef": "tsdevmdl.vc",
        //              "version": "3.0"
        //            },
        //            {
        //              "@context": [
        //                "https://www.w3.org/2018/credentials/v1"
        //              ],
        //              "assertion": {
        //                "code": "devtestmdl2",
        //                "desc": "로컬개발테스트",
        //                "name": "로컬개발테스트"
        //              },
        //              "credentialSubject": {
        //                "id": "did:kr:mobileid:3eF1sEPSEoL1r4MPtoEQsXDPRSKo",
        //                "privacy": [
        //                  {
        //                    "type": "name",
        //                    "value": "정진우"
        //                  }
        //                ]
        //              },
        //              "encoding": "UTF-8",
        //              "evidence": [
        //                {
        //                  "documentPresence": "Physical",
        //                  "evidenceDocument": "4",
        //                  "subjectPresence": "Physical",
        //                  "type": "DocumentVerification",
        //                  "verifier": "Issuer"
        //                }
        //              ],
        //              "expirationDate": "2027-12-19T23:59:59",
        //              "id": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "issuanceDate": "2024-12-19T10:48:08",
        //              "issuer": {
        //                "desc": "",
        //                "id": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3",
        //                "name": "운전면허증-테스트베드DEV"
        //              },
        //              "language": "KR",
        //              "parentId": "df2b3abc-fc80-4703-ae78-92d317fa7b1d",
        //              "proof": {
        //                "created": "2024-12-19T10:48:08",
        //                "creator": "did:kr:mobileid:dVmHQbX8F4M7cH5PVtLBaEWnJk3#tss.devmdl01",
        //                "signatureValue": "SIG_R1_L2LFqVvJx9bYzMQim1UwYfpvXBZjdPeW7a55hsULw9uwmNeWkmxjxq4e3V3kchL3dzjFKnym2HaEb5NGSx7FYHT9oHwrnv",
        //                "type": "Secp256r1VerificationKey2018"
        //              },
        //              "signKeyId": "tss.devmdl01",
        //              "timezone": "UTC+9",
        //              "type": [
        //                "VerifiableCredential",
        //                "OmniOneCredential"
        //              ],
        //              "vcDef": "tsdevmdl.vc",
        //              "version": "3.0"
        //            }
        //          ]
        //        }
        //        """;
        //ObjectMapper objectMapper = new ObjectMapper();
        //// String을 JsonNode로 변환
        //root = objectMapper.readTree(jsonString);
        // expirationDate, issuanceDate, issuer의 name 추출
        JsonNode verifiableCredentialNode = root.get("verifiableCredential");
        if (verifiableCredentialNode != null && verifiableCredentialNode.isArray()) {
            for (JsonNode credential : verifiableCredentialNode) {
                // expirationDate, issuanceDate
                String expirationDate = credential.path("expirationDate").asText();
                String issuanceDate = credential.path("issuanceDate").asText();

                // issuer.name
                String issuerName = credential.path("issuer").path("name").asText();

                // credentialSubject의 type과 value 추출
                JsonNode privacyNode = credential.path("credentialSubject").path("privacy");
                if (privacyNode != null && privacyNode.isArray()) {
                    for (JsonNode item : privacyNode) {
                        String type = item.path("type").asText();
                        String value = item.path("value").asText();
                        extractedData.put(type, value); // type을 키로, value를 값으로 추가
                    }
                }

                // expirationDate, issuanceDate, issuerName 저장
                extractedData.put("expirationDate", expirationDate);
                extractedData.put("issuanceDate", issuanceDate);
                extractedData.put("issuerName", issuerName);
            }
        }
        log.info("extractedData: {}", extractedData);

        return extractedData;
    }

}
