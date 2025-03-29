package egovframework.kt.dxp.api.domain.citizen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.IssuerType;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.PrivacyType;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.ServiceEndpoint;
import egovframework.kt.dxp.api.domain.citizen.MobileCitizenEnum.Status;
import egovframework.kt.dxp.api.domain.citizen.record.CredentialData;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenBookmarkRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenDetailRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenManualRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenOrderRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenResponse;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStartCommonRecord;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStartRecord;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStatusRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenStatusResponse;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenVPRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenVpDataRequest;
import egovframework.kt.dxp.api.domain.citizen.record.MobileCitizenVpDataResponse;
import egovframework.kt.dxp.api.domain.citizen.record.StartApp2AppRequest;
import egovframework.kt.dxp.api.domain.citizen.record.VerifiablePresentation;
import egovframework.kt.dxp.api.domain.citizen.record.VerifiablePresentation.VerifiableCredential.CredentialSubject.Privacy;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.L_MOBILE_ID;
import egovframework.kt.dxp.api.entity.M_MOBILE_ID;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.TB_TRX_INFO;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_MOBILE_ID_KEY;
import egovframework.kt.dxp.api.entity.key.M_MOBILE_ID_KEY;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class MobileCitizenService {

    private final MessageConfig messageConfig;
    private final UserRepository userRepository;
    private final MobileCitizenMasterRepository masterRepository;
    private final MobileCitizenHistoryRepository historyRepository;
    private final MobileCitizenTransactionRepository transactionRepository;

    private final Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.profiles.active:dev}")
    private String profileCode;

    @Value("${sp.did.service-code:chuncheon.1}")
    private String serviceCode;

    @Value("${sp.did.base-url:http://211.41.186.152:13345}")
    private String baseUrl;

    @Transactional
    public ItemResponse<MobileCitizenResponse> startApp2App(MobileCitizenRequest request) throws Exception {
        JsonNode baseData = null;
        // 요청에 따라 동적으로 처리하게 해야하나?
        // 현 App2App만 존재한 상황
        ServiceEndpoint serviceEndpoint = ServiceEndpoint.APP2APP_START;
        IdentityCode identityCode = IdentityCode.fromCode(request.identityCode());
        String userId = CommonUtils.getUserId();

        StartApp2AppRequest startApp2AppRequest = StartApp2AppRequest.builder()
                                                                     .cmd(serviceEndpoint.getCommand())
                                                                     .mode(request.mode())
                                                                     .svcCode(serviceCode)
                                                                     .build();

        Mono<MobileCitizenResponse> response = sendPostRequest(baseUrl, startApp2AppRequest, serviceEndpoint,
                MobileCitizenResponse.class);

        // Mono를 block()으로 처리하여 동기적으로 결과를 받음
        MobileCitizenResponse mobileCitizenResponse = response.block();
        if (mobileCitizenResponse == null) {
            return ItemResponse.<MobileCitizenResponse>builder()
                               .status(messageConfig.getCode(ErrorCode.NO_DATA))
                               .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                               .build();
        }

        // TODO: 응답온 데이터 기반으로 DB 처리 로직 추가
        // JSON 파싱
        JsonNode jsonNode = getJson(mobileCitizenResponse.data());
        // JsonNode를 record로 변환
        MobileCitizenStartRecord citizenStartRecord = objectMapper.treeToValue(jsonNode,
                MobileCitizenStartRecord.class);

        if (citizenStartRecord.m120Base64() != null) {
            baseData = getJson(citizenStartRecord.m120Base64());
        }

        if (citizenStartRecord.m200Base64() != null) {
            baseData = getJson(citizenStartRecord.m200Base64());
        }

        if (baseData == null) {
            return ItemResponse.<MobileCitizenResponse>builder()
                               .status(messageConfig.getCode(ErrorCode.NO_DATA))
                               .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                               .item(mobileCitizenResponse)
                               .build();
        }
        MobileCitizenStartCommonRecord mobileCitizenStartCommonRecord = objectMapper.treeToValue(baseData,
                MobileCitizenStartCommonRecord.class);
        // mUsr.updateTrxCode(mobileCitizenStartCommonRecord.trxcode());
        // userRepository.save(mUsr);
        M_MOBILE_ID_KEY mobileIdKey = M_MOBILE_ID_KEY.builder()
                                                     .userId(userId)
                                                     .trxCode(mobileCitizenStartCommonRecord.trxcode())
                                                     .build();
        M_MOBILE_ID mMobileId = M_MOBILE_ID.builder()
                                           .key(mobileIdKey)
                                           .identityCode(identityCode.getCode())
                                           .chuncheonYn(UseYn.N)
                                           .bookmarkYn(UseYn.N)
                                           .approvalTypeCode("AUTO")
                                           .issuedStatusCode(Status.REQUESTED.name())
                                           .processUserId("SYSTEM")
                                           .build();

        L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                      .userId(mMobileId.getKey().getUserId())
                                                      .trxCode(mMobileId.getKey().getTrxCode())
                                                      .processSequenceNumber(1)
                                                      .build();
        L_MOBILE_ID lMobileId = L_MOBILE_ID.builder()
                                           .key(lMobileIdKey)
                                           .identityCode(mMobileId.getIdentityCode())
                                           .statusCode(mMobileId.getIssuedStatusCode())
                                           .build();
        masterRepository.save(mMobileId);
        historyRepository.save(lMobileId);

        // JSON 변환
        // String json = objectMapper.writeValueAsString(citizenStartRecord);
        // log.info("Record data: {}", json);
        // log.info("data: {}",mobileCitizenResponse.data());
        // log.info("result: {}",mobileCitizenResponse.result());
        // mUsr.updateTrxCode();
        //// 비동기 처리
        // response.subscribe(data -> {
        // // 응답 처리
        // mobileCitizenResponse.builder()
        // .result(data.result())
        // .data(data.data())
        // .build();
        // });
        return ItemResponse.<MobileCitizenResponse>builder()
                           .status(messageConfig.getCode(NormalCode.REQUEST_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.REQUEST_SUCCESS))
                           .item(mobileCitizenResponse)
                           .build();
    }

    @Transactional
    public ItemResponse<Long> startManual(MobileCitizenManualRequest request) throws Exception {

        IdentityCode identityCode = IdentityCode.fromCode(request.identityCode());
        String userId = CommonUtils.getUserId();

        // TODO: M_MOBILE_ID 기존에 발급 받은 신분증이 있는지 확인하는 로직 추가 필요
        Optional<M_MOBILE_ID> mMobileIdOptional = masterRepository
                .findTopByKeyUserIdAndIdentityCodeAndIssuedStatusCodeOrderByCreateDateDesc(userId,
                        identityCode.getCode(), Status.APPROVED.name());
        if (mMobileIdOptional.isPresent()) {
            mMobileIdOptional.get().updateMobileIdStatus(Status.REVOKED.name());
            // 이력 수 확인
            Integer historyCount = getHistoryCount(userId, mMobileIdOptional.get().getKey().getTrxCode());

            L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                          .userId(mMobileIdOptional.get().getKey().getUserId())
                                                          .trxCode(mMobileIdOptional.get().getKey().getTrxCode())
                                                          .processSequenceNumber(historyCount + 1)
                                                          .build();
            L_MOBILE_ID lMobileId = L_MOBILE_ID.builder()
                                               .key(lMobileIdKey)
                                               .identityCode(mMobileIdOptional.get().getIdentityCode())
                                               .statusCode(Status.REVOKED.name())
                                               .build();
            masterRepository.save(mMobileIdOptional.get());
            historyRepository.save(lMobileId);
        }

        M_MOBILE_ID_KEY mobileIdKey = M_MOBILE_ID_KEY.builder()
                                                     .userId(userId)
                                                     .trxCode(genTrxcode())
                                                     .build();
        M_MOBILE_ID mMobileId = M_MOBILE_ID.builder()
                                           .key(mobileIdKey)
                                           .identityCode(identityCode.getCode())
                                           .address("춘천시 " + request.dongName())
                                           .chuncheonYn(UseYn.N)
                                           .bookmarkYn(UseYn.N)
                                           .approvalTypeCode("MANUAL")
                                           .issuedStatusCode(Status.REQUESTED.name())
                                           .processUserId("SYSTEM")
                                           .build();

        L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                      .userId(mMobileId.getKey().getUserId())
                                                      .trxCode(mMobileId.getKey().getTrxCode())
                                                      .processSequenceNumber(1)
                                                      .build();
        L_MOBILE_ID lMobileId = L_MOBILE_ID.builder()
                                           .key(lMobileIdKey)
                                           .identityCode(mMobileId.getIdentityCode())
                                           .statusCode(mMobileId.getIssuedStatusCode())
                                           .build();
        masterRepository.save(mMobileId);
        historyRepository.save(lMobileId);

        return ItemResponse.<Long>builder()
                           .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                           .item(1L)
                           .build();
    }

    @Transactional
    public ItemResponse<MobileCitizenVpDataResponse> vpSubmit(MobileCitizenVPRequest request) throws Exception {
        String address = null;
        String name = null;
        String birth = null;
        String certificationId = null;
        // VC 정보 저장 변수
        LocalDateTime issuanceDateTime = null;
        LocalDateTime expirationDateTime = null;
        UseYn isExpired = UseYn.N;
        String issuanceDate = null;
        String expirationDate = null;
        String assertionName = null;
        Status status = Status.REJECTED;
        UseYn isChuncheonAddress = UseYn.N; // 기본값 N으로 설정
        IssuerType issuerType = IssuerType.UNKNOWN;
        Mono<MobileCitizenResponse> response;
        LocalDateTime now = LocalDateTime.now();
        String userId = CommonUtils.getUserId();
        MobileCitizenVpDataResponse mobileCitizenVpDataSuccessResponse;

        IdentityCode identityCode = IdentityCode.fromCode(request.identityCode());

        // data 필드 값이 존재하는 경우 indirect mode
        if (isValidVpData(request)) {
            // JSON 파싱
            JsonNode jsonNode = getJson(request.data());
            log.info("Perform indirect validation. data: [{}]", jsonNode);
            // "mode" 키가 존재하고 값이 "indirect"인 경우 처리
            // Note: 아직 indirect 데이터를 확인 못 해서 확인시 데이터 검증 해야함
            if (jsonNode.has("mode") && "indirect".equals(jsonNode.get("mode").asText())) {
                // indirect면 검증을 해야함
                response = sendPostRequest(baseUrl, request, ServiceEndpoint.MIP_VP, MobileCitizenResponse.class);
            }
        }
        //// "mode" 키가 존재하고 값이 "direct"인 경우 처리
        // if (jsonNode.has("mode") && "direct".equals(jsonNode.get("mode").asText())) {
        // // direct면 검증이 완료된 데이터인데 다시 검증할 필요가 있을까?
        // response = sendPostRequest(baseUrl, request, ServiceEndpoint.MIP_REVP,
        //// MobileCitizenResponse.class);
        // }

        Optional<M_MOBILE_ID> mMobileIdOptional = masterRepository
                .findTopByKeyUserIdAndIssuedStatusCodeOrderByCreateDateDesc(userId, Status.REQUESTED.name());
        if (mMobileIdOptional.isEmpty()) {
            MobileCitizenVpDataResponse mobileCitizenVpDataErrorResponse = MobileCitizenVpDataResponse.builder()
                                                                                                      .result(false)
                                                                                                      .isChuncheonAddress(isChuncheonAddress)
                                                                                                      .build();
            return ItemResponse.<MobileCitizenVpDataResponse>builder()
                               .status(messageConfig.getCode(ErrorCode.NO_DATA))
                               .message(messageConfig.getMessage(ErrorCode.NO_DATA))
                               .item(mobileCitizenVpDataErrorResponse)
                               .build();
        }

        Optional<TB_TRX_INFO> tbTrxInfo = transactionRepository.findById(mMobileIdOptional.get().getKey().getTrxCode());
        if (tbTrxInfo.isPresent() && UseYn.N.equals(tbTrxInfo.get().getVpVerifyResult())) {
            MobileCitizenVpDataResponse mobileCitizenVpDataErrorResponse = MobileCitizenVpDataResponse.builder()
                                                                                                      .result(false)
                                                                                                      .isChuncheonAddress(isChuncheonAddress)
                                                                                                      .build();
            return ItemResponse.<MobileCitizenVpDataResponse>builder()
                               .status(messageConfig.getCode(ErrorCode.DATA_PROCESSING_ERROR))
                               .message(messageConfig.getMessage(ErrorCode.DATA_PROCESSING_ERROR))
                               .item(mobileCitizenVpDataErrorResponse)
                               .build();
        }
        // ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // VP JSON 생성
        ObjectNode vpNode = objectMapper.createObjectNode();
        vpNode.put("vp", tbTrxInfo.get().getVp());
        // JsonNode를 String으로 변환
        String vpJsonString = objectMapper.writeValueAsString(vpNode);
        MobileCitizenVpDataRequest mobileCitizenVpDataRequest = MobileCitizenVpDataRequest.builder()
                                                                                          .data(encodeToBase64(String.valueOf(vpJsonString)))
                                                                                          .build();
        // Note: VP 데이터 복호화 요청
        response = sendPostRequest(baseUrl, mobileCitizenVpDataRequest, ServiceEndpoint.MIP_VPDATA,
                MobileCitizenResponse.class);
        MobileCitizenResponse mobileCitizenResponse = response.block();
        if(!mobileCitizenResponse.result()){
            MobileCitizenVpDataResponse mobileCitizenVpDataErrorResponse = MobileCitizenVpDataResponse.builder()
                                                                                                      .result(false)
                                                                                                      .isChuncheonAddress(isChuncheonAddress)
                                                                                                      .build();
            return ItemResponse.<MobileCitizenVpDataResponse>builder()
                               .status(messageConfig.getCode(ErrorCode.DATA_PROCESSING_ERROR))
                               .message(messageConfig.getMessage(ErrorCode.DATA_PROCESSING_ERROR))
                               .item(mobileCitizenVpDataErrorResponse)
                               .build();
        }

        JsonNode jsonNode = getJson(mobileCitizenResponse.data());


        // JsonNode를 VerifiablePresentation으로 변환
        VerifiablePresentation verifiablePresentation = objectMapper.treeToValue(jsonNode,
                VerifiablePresentation.class);
        log.info("Decoded VP: {}", verifiablePresentation);

        // 사용자 CI 가져오기
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));
        String userCertificationId = mUsr.getCertificationId();

        int ciLength = 64;
        String targetCertificationId = "";
        String mobilePhoneNumber = "";
        if (verifiablePresentation.proof().nonce().length() > ciLength) {
            certificationId = new String(toBytes(verifiablePresentation.proof().nonce().substring(ciLength)));

            if (!certificationId.contains("&")) {
                targetCertificationId = certificationId;
            } else {
                targetCertificationId = certificationId.split("&")[0];
            }
            if (certificationId.contains("&")) {
                mobilePhoneNumber = certificationId.split("&")[1];
            }
        }

        boolean ciStatus = true;
        // 개발, 운영계 CI 값 비교 확인 테스트
        if (!targetCertificationId.equals(userCertificationId)) {
            mMobileIdOptional.get().updateRemark("CI 값이 일치하지 않습니다.");
            ciStatus = false;
        }

        // 필요한 데이터 추출
        for (VerifiablePresentation.VerifiableCredential vc : verifiablePresentation.verifiableCredential()) {
            // VC 메타데이터 추출
            issuanceDate = vc.issuanceDate();
            expirationDate = vc.expirationDate();
            assertionName = vc.assertion() != null ? vc.assertion().name() : null;
            issuerType = IssuerType.fromIssuerName(assertionName);
            log.info("VC Metadata - IssuanceDate: {}, ExpirationDate: {}, assertionName: {}", issuanceDate,
                    expirationDate, assertionName);

            List<Privacy> privacyData = vc.credentialSubject().privacy();
            for (Privacy privacy : privacyData) {
                switch (PrivacyType.fromString(privacy.type())) {
                    case ADDRESS -> {
                        address = privacy.value();
                        // 주소에 "춘천" 포함 여부 확인
                        if (address != null && address.contains("춘천")) {
                            isChuncheonAddress = UseYn.Y;
                        }
                    }
                    case NAME -> name = privacy.value();
                    case BIRTH -> birth = privacy.value();
                    case UNKNOWN -> log.warn("Unexpected privacy type: {}", privacy.type());
                    default -> log.warn("Unhandled privacy type: {}, {}", privacy.type(), privacy.value());
                }
                // Note: 운영시 로그 삭제
                log.info("Privacy {}: {}", privacy.type(), privacy.value());
            }
        }
        // Note: 운영시 로그 삭제
        log.info("Extracted values - Name: {}, Birth: {}, Address: {}, isChuncheonAddress: {}", name, birth, address,
                isChuncheonAddress);
        issuanceDateTime = parseDateTime(issuanceDate);
        expirationDateTime = parseDateTime(expirationDate);

        // TODO: 응답온 데이터 기반으로 DB 처리 로직 추가
        if (UseYn.Y.equals(isChuncheonAddress)) {
            status = Status.APPROVED;
            mobileCitizenVpDataSuccessResponse = MobileCitizenVpDataResponse.builder()
                                                                            .result(true)
                                                                            .isChuncheonAddress(isChuncheonAddress)
                                                                            .build();
        } else {
            mobileCitizenVpDataSuccessResponse = MobileCitizenVpDataResponse.builder()
                                                                            .result(false)
                                                                            .isChuncheonAddress(isChuncheonAddress)
                                                                            .build();
        }

        if (expirationDateTime != null && !now.isAfter(expirationDateTime)) {
            // 유효기간이 만료되지 않은 경우 3년 연장
            expirationDateTime = now.plusYears(3);
            // TODO: M_MOBILE_ID 기존에 발급 받은 신분증이 있는지 확인하는 로직 추가 필요
            Optional<M_MOBILE_ID> mMobileId = masterRepository
                    .findTopByKeyUserIdAndIdentityCodeAndIssuedStatusCodeOrderByCreateDateDesc(userId,
                            identityCode.getCode(), Status.APPROVED.name());
            if (mMobileId.isPresent()) {
                mMobileId.get().updateMobileIdStatus(Status.REVOKED.name());
                // 이력 수 확인
                Integer historyCount = getHistoryCount(userId, mMobileId.get().getKey().getTrxCode());

                L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                              .userId(mMobileId.get().getKey().getUserId())
                                                              .trxCode(mMobileId.get().getKey().getTrxCode())
                                                              .processSequenceNumber(historyCount + 1)
                                                              .build();
                L_MOBILE_ID lMobileId = L_MOBILE_ID.builder()
                                                   .key(lMobileIdKey)
                                                   .identityCode(mMobileId.get().getIdentityCode())
                                                   .statusCode(Status.REVOKED.name())
                                                   .build();
                masterRepository.save(mMobileId.get());
                historyRepository.save(lMobileId);
            }
        } else {
            isExpired = UseYn.Y;
            status = Status.EXPIRED;
        }

        // Note: mobilePhoneNumber는 나중에 확인되면 넣기
        mMobileIdOptional.get().updateMobileIdInformation(address, isChuncheonAddress, assertionName,
                issuerType.getCode(), status.name(), issuanceDateTime, expirationDateTime, "SYSTEM", mobilePhoneNumber);

        // 이력 수 확인
        Integer historyCount = getHistoryCount(userId, mMobileIdOptional.get().getKey().getTrxCode());

        L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                      .userId(mMobileIdOptional.get().getKey().getUserId())
                                                      .trxCode(mMobileIdOptional.get().getKey().getTrxCode())
                                                      .processSequenceNumber(historyCount + 1)
                                                      .build();
        L_MOBILE_ID lMobileId;

        if (status.equals(Status.APPROVED) || status.equals(Status.REJECTED)) {
            lMobileId = L_MOBILE_ID.builder()
                                   .key(lMobileIdKey)
                                   .identityCode(mMobileIdOptional.get().getIdentityCode())
                                   .identityTitleCode(mMobileIdOptional.get().getIdentityTitleCode())
                                   .identityName(mMobileIdOptional.get().getIdentityName())
                                   .statusCode(status.name())
                                   .build();
        } else {
            lMobileId = L_MOBILE_ID.builder()
                                   .key(lMobileIdKey)
                                   .identityCode(mMobileIdOptional.get().getIdentityCode())
                                   .statusCode(status.name())
                                   .build();

        }

        masterRepository.save(mMobileIdOptional.get());
        historyRepository.save(lMobileId);
        return ItemResponse.<MobileCitizenVpDataResponse>builder()
                           .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                           .item(UseYn.Y.equals(isExpired) ? MobileCitizenVpDataResponse.builder()
                                                                                        .result(false)
                                                                                        .isChuncheonAddress(isChuncheonAddress) // 동적으로 변경해야함
                                                                                        .build() : mobileCitizenVpDataSuccessResponse)
                           .build();
    }

    @Transactional
    public ItemsResponse<CredentialData> getSearchList() {
        List<CredentialData> credentialDataList = new ArrayList<>();
        CredentialData credentialData = null;

        LocalDateTime currentDateTime = LocalDateTime.now();
        String userId = CommonUtils.getUserId();
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));

        List<String> statusList = Arrays.asList(Status.APPROVED.name(), Status.EXPIRED.name());

        List<M_MOBILE_ID> mobileIds = masterRepository
                .findByKeyUserIdAndIssuedStatusCodeInOrderByBookmarkYnDescDisplaySequenceDescCreateDateDesc(
                        userId, statusList);
        if (!mobileIds.isEmpty()) {
            // TODO: mobileIds 리스트 사이즈 만큼 반복하면서 credentialData에 값을 세팅
            for (M_MOBILE_ID mobileId : mobileIds) {
                credentialData = CredentialData.builder()
                                               .approvalTypeCode(mobileId.getApprovalTypeCode())
                                               .trxCode(mobileId.getKey().getTrxCode())
                                               .expirationDate(mobileId.getValidDate() != null
                                                       ? Converter.localDateTimeToString(
                                                       mobileId.getValidDate(),
                                                       DateType.YYYYMMDD_FORMAT)
                                                       : null)
                                               .issuanceDate(mobileId.getApprovalDate() != null
                                                       ? Converter.localDateTimeToString(
                                                       mobileId.getApprovalDate(),
                                                       DateType.YYYYMMDD_FORMAT)
                                                       : null)
                                               .issuerName(IdentityCode.fromCode(
                                                       mobileId.getIdentityCode()).getDescription())
                                               .issuerCode(mobileId.getIdentityCode())
                                               .isIssued(UseYn.Y)
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
        // Check and add default credentials if not present

        // IdentityCode Enum의 모든 항목을 가져옴
        List<IdentityCode> requiredIssuerCodes = IdentityCode.getAllIdentityCodes();
        Set<String> existingIssuerCodes = credentialDataList.stream()
                                                            .map(CredentialData::issuerCode)
                                                            .collect(Collectors.toSet());

        addMissingIssuerCodes(credentialDataList, existingIssuerCodes, requiredIssuerCodes);

        return ItemsResponse.<CredentialData>builder()
                            .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                            .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                            .items(credentialDataList)
                            .totalSize((long) credentialDataList.size())
                            .build();
    }

    @Transactional
    public ItemResponse<CredentialData> getSearchDetailList(MobileCitizenDetailRequest request) {
        CredentialData credentialData = null;

        LocalDateTime currentDateTime = LocalDateTime.now();
        String userId = CommonUtils.getUserId();
        M_USR mUsr = userRepository.findById(userId)
                                   .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA, userId + " doesn't exist."));

        Optional<M_MOBILE_ID> mobileId = masterRepository.findByKeyUserIdAndKeyTrxCode(userId, request.trxCode());
        if (mobileId.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        credentialData = CredentialData.builder()
                                       .approvalTypeCode(mobileId.get().getApprovalTypeCode())
                                       .trxCode(mobileId.get().getKey().getTrxCode())
                                       .expirationDate(mobileId.get().getValidDate() != null
                                               ? Converter.localDateTimeToString(
                                               mobileId.get().getValidDate(),
                                               DateType.YYYYMMDD_FORMAT)
                                               : null)
                                       .issuanceDate(mobileId.get().getApprovalDate() != null
                                               ? Converter.localDateTimeToString(
                                               mobileId.get().getApprovalDate(),
                                               DateType.YYYYMMDD_FORMAT)
                                               : null)
                                       .issuerName(IdentityCode.fromCode(
                                               mobileId.get().getIdentityCode()).getDescription())
                                       .issuerCode(mobileId.get().getIdentityCode())
                                       .isChuncheonAddress(mobileId.get().getChuncheonYn())
                                       .bookmarkYn(mobileId.get().getBookmarkYn())
                                       .name(mUsr.getUserName())
                                       .address(mobileId.get().getAddress())
                                       .birth(mUsr.getBirthDate())
                                       .build();

        return ItemResponse.<CredentialData>builder()
                           .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                           .item(credentialData)
                           .build();
    }

    @Transactional
    public ItemResponse<Long> deleteDid(MobileCitizenDetailRequest request) {
        Status status = Status.REVOKED;
        String userId = CommonUtils.getUserId();
        Optional<M_MOBILE_ID> mobileId = masterRepository.findByKeyUserIdAndKeyTrxCode(userId, request.trxCode());
        if (mobileId.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        mobileId.get().deleteMobileIdInformation(String.valueOf(status));

        return ItemResponse.<Long>builder()
                           .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                           .item(1L)
                           .build();
    }

    @Transactional
    public ItemResponse<Long> updateDidOrder(MobileCitizenOrderRequest request) {
        String userId = CommonUtils.getUserId();

        List<MobileCitizenOrderRequest.Order> orders = request.order();
        if (orders == null || orders.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        List<String> trxCodes = orders.stream()
                                      .map(MobileCitizenOrderRequest.Order::trxCode)
                                      .collect(Collectors.toList());

        List<M_MOBILE_ID> mobileIds = masterRepository.findByKeyUserIdAndKeyTrxCodeIn(userId, trxCodes);
        if (mobileIds.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        // Update bookmark and display order for each mobile ID based on the order list
        for (MobileCitizenOrderRequest.Order order : orders) {
            mobileIds.stream()
                     .filter(mobileId -> mobileId.getKey().getTrxCode().equals(order.trxCode()))
                     .findFirst()
                     .ifPresent(mobileId -> mobileId.updateMobileIdOrder(order.bookmarkYn(),
                             Integer.parseInt(order.displaySequence())));
        }
        return ItemResponse.<Long>builder()
                           .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                           .item((long) orders.size())
                           .build();
    }

    @Transactional
    public ItemResponse<MobileCitizenStatusResponse> manageDid(MobileCitizenStatusRequest request) {
        String userId = CommonUtils.getUserId();

        if (!request.isDelete()) {
            // Note: 모바일 신분증 조회
            Optional<M_MOBILE_ID> mobileId = masterRepository
                    .findTopByKeyUserIdAndIdentityCodeAndIssuedStatusCodeAndApprovalTypeCodeOrderByCreateDateDesc(
                            userId,
                            request.issuerCode(),
                            Status.REQUESTED.name(),
                            request.approvalTypeCode());
            if (mobileId.isEmpty()) {
                return ItemResponse.<MobileCitizenStatusResponse>builder()
                                   .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                                   .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                                   .item(null)
                                   .build();
            }

            MobileCitizenStatusResponse mobileCitizenStatusResponse = MobileCitizenStatusResponse.builder()
                                                                                                 .issuerCode(mobileId.get().getIdentityCode())
                                                                                                 .trxCode(mobileId.get().getKey().getTrxCode())
                                                                                                 .issuedStatusCode(mobileId.get().getIssuedStatusCode())
                                                                                                 .createDate(mobileId.get().getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                                                                                                 .build();
            return ItemResponse.<MobileCitizenStatusResponse>builder()
                               .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                               .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                               .item(mobileCitizenStatusResponse)
                               .build();
        } else {
            // Note: 모바일 신분증 삭제
            Optional<M_MOBILE_ID> mobileId = masterRepository.findByKeyUserIdAndKeyTrxCode(userId, request.trxCode());
            if (mobileId.isEmpty()) {
                return ItemResponse.<MobileCitizenStatusResponse>builder()
                                   .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                                   .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                                   .item(null)
                                   .build();
            }

            mobileId.get().updateMobileIdStatus(Status.REVOKED.name());
            masterRepository.save(mobileId.get());

            // 이력 수 확인
            Integer historyCount = getHistoryCount(userId, request.trxCode());

            L_MOBILE_ID_KEY lMobileIdKey = L_MOBILE_ID_KEY.builder()
                                                          .userId(userId)
                                                          .trxCode(request.trxCode())
                                                          .processSequenceNumber(historyCount + 1)
                                                          .build();

            L_MOBILE_ID lMobileId = L_MOBILE_ID.builder()
                                               .key(lMobileIdKey)
                                               .identityCode(mobileId.get().getIdentityCode())
                                               .statusCode(Status.REVOKED.name())
                                               .build();

            historyRepository.save(lMobileId);
            MobileCitizenStatusResponse mobileCitizenStatusResponse = MobileCitizenStatusResponse.builder()
                                                                                                 .issuerCode(mobileId.get().getIdentityCode())
                                                                                                 .trxCode(mobileId.get().getKey().getTrxCode())
                                                                                                 .issuedStatusCode(Status.REVOKED.name())
                                                                                                 .createDate(mobileId.get().getCreateDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                                                                                                 .build();
            return ItemResponse.<MobileCitizenStatusResponse>builder()
                               .status(messageConfig.getCode(NormalCode.DELETE_SUCCESS))
                               .message(messageConfig.getMessage(NormalCode.DELETE_SUCCESS))
                               .item(mobileCitizenStatusResponse)
                               .build();
        }
    }

    @Transactional
    public ItemResponse<Integer> getModifyDidBookmark(MobileCitizenBookmarkRequest parameter) {
        String userId = CommonUtils.getUserId();
        String trxCode = parameter.trxCode();
        String bookmarkYn = parameter.bookmarkYn();

        M_MOBILE_ID_KEY key = M_MOBILE_ID_KEY.builder()
                                             .userId(userId)
                                             .trxCode(trxCode)
                                             .build();

        // 승인된 모든 모바일 ID 조회
        List<M_MOBILE_ID> mobileIds = masterRepository
                .findByKeyUserIdAndIssuedStatusCodeOrderByBookmarkYnDescCreateDateDescDisplaySequenceDesc(
                        userId, Status.APPROVED.name());

        // 북마크 상태 업데이트
        mobileIds.stream()
                 .filter(data -> data.getKey().getTrxCode().equals(trxCode))
                 .findFirst()
                 .ifPresent(data -> data.updateBookmark(key, UseYn.valueOf(bookmarkYn)));

        // 정렬 및 순서 재배치
        List<M_MOBILE_ID> sortedList = new ArrayList<>();

        if (UseYn.Y.name().equals(bookmarkYn)) {
            // 북마크 추가(Y) 케이스
            // 1. 요청된 trxCode 항목을 최상위로
            mobileIds.stream()
                     .filter(data -> data.getKey().getTrxCode().equals(trxCode))
                     .findFirst()
                     .ifPresent(data -> {
                         data.setDisplaySequence(mobileIds.size() - 1);
                         sortedList.add(data);
                     });

            // 2. 나머지 북마크된 항목들을 DisplaySequence 내림차순으로
            mobileIds.stream()
                     .filter(data -> !data.getKey().getTrxCode().equals(trxCode))
                     .filter(data -> UseYn.Y.equals(data.getBookmarkYn()))
                     // .sorted(Comparator.comparing(M_MOBILE_ID::getCreateDate).reversed())
                     .sorted(Comparator.comparing(M_MOBILE_ID::getDisplaySequence).reversed())
                     .forEach(sortedList::add);

            // 3. 북마크되지 않은 항목들도 생성일자 내림차순으로
            mobileIds.stream()
                     .filter(data -> UseYn.N.equals(data.getBookmarkYn()))
                     .sorted(Comparator.comparing(M_MOBILE_ID::getCreateDate).reversed())
                     .forEach(sortedList::add);
        } else {
            // 북마크 제거(N) 케이스
            // 1. 기존 북마크된 항목들을 DisplaySequence 내림차순으로
            mobileIds.stream()
                     .filter(data -> UseYn.Y.equals(data.getBookmarkYn()))
                     // .sorted(Comparator.comparing(M_MOBILE_ID::getDisplaySequence).reversed()
                     // .thenComparing(M_MOBILE_ID::getCreateDate, Comparator.reverseOrder()))
                     .sorted(Comparator.comparing(M_MOBILE_ID::getDisplaySequence).reversed())
                     .forEach(sortedList::add);

            // 2. 북마크 해제된 항목들도 생성일자 내림차순으로
            mobileIds.stream()
                     .filter(data -> UseYn.N.equals(data.getBookmarkYn()))
                     .sorted(Comparator.comparing(M_MOBILE_ID::getCreateDate).reversed())
                     .forEach(sortedList::add);
        }

        // 최종 순서 부여
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setDisplaySequence(sortedList.size() - 1 - i);
        }

        // 저장
        masterRepository.saveAllAndFlush(sortedList);

        return ItemResponse.<Integer>builder()
                           .status(messageConfig.getCode(NormalCode.MODIFY_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.MODIFY_SUCCESS))
                           .item(1)
                           .build();
    }

    // WebClient를 사용하여 POST 요청을 보내는 제네릭 함수 (예외 처리 추가)
    public <T> Mono<T> sendPostRequest(String baseUrl, Object request, ServiceEndpoint serviceEndpoint,
            Class<T> clazz) {
        return webClientBuilder.baseUrl(baseUrl) // Base URL 설정
                               .build()
                               .post() // POST 요청
                               .uri(serviceEndpoint.getUrl()) // URI 설정
                               .bodyValue(request) // 요청 body 설정
                               .retrieve() // 응답을 받아옴
                               .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                       clientResponse -> Mono.error(new WebClientResponseException(clientResponse.rawStatusCode(),
                                               "HTTP error: " + clientResponse.statusCode(),
                                               clientResponse.headers().asHttpHeaders(), null, null)))
                               .bodyToMono(clazz) // 응답을 제네릭 타입으로 변환
                               .onErrorResume(e -> {
                                   // 예외가 발생하면 적절히 처리 (예: 로그 기록, 기본값 반환 등)
                                   if (e instanceof WebClientResponseException) {
                                       WebClientResponseException ex = (WebClientResponseException) e;
                                       return Mono.error(new RuntimeException("Error during HTTP request: " + ex.getMessage(), ex));
                                   }
                                   return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage(), e));
                               });
    }

    // Base64 인코딩 함수
    public static String encodeToBase64(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        return Base64Utils.encodeToUrlSafeString(input.getBytes());
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

    /**
     * VP request의 data 필드 유효성 검사
     *
     * @param request VP 요청 객체
     * @return true if data exists and is not empty, false otherwise
     */
    private boolean isValidVpData(MobileCitizenVPRequest request) {
        return request != null && request.data() != null && !request.data().trim().isEmpty();
    }

    /**
     * ISO 8601 형식의 날짜 문자열을 LocalDateTime으로 변환
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse datetime string: {}", dateTimeStr, e);
            return null;
        }
    }

    /**
     * 사용자의 특정 거래에 대한 이력 수 확인
     *
     * @param userId  사용자 ID
     * @param trxCode 거래 코드
     * @return 이력 수
     */
    private Integer getHistoryCount(String userId, String trxCode) {
        return historyRepository.countByKeyUserIdAndKeyTrxCode(userId, trxCode);
    }

    // 메서드: credentialDataList에 requiredIssuerCodes에 포함된 issuerCode가 없으면 추가
    public void addMissingIssuerCodes(List<CredentialData> credentialDataList, Set<String> existingIssuerCodes, List<IdentityCode> requiredIssuerCodes) {
        // Note: 임시로 운영일 때만 M01 데이터 전송
        if(profileCode.equals("dev")) {
            for (IdentityCode identityCode : requiredIssuerCodes) {
                String issuerCode = identityCode.getCode();
                if (!existingIssuerCodes.contains(issuerCode)) {
                    // 새로운 CredentialData 객체 생성
                    CredentialData credentialData = CredentialData.builder()
                                                                  .issuerCode(issuerCode)
                                                                  .issuerName(IdentityCode.fromCode(issuerCode).getDescription())
                                                                  .isIssued(UseYn.N)
                                                                  .build();

                    // credentialDataList에 추가
                    credentialDataList.add(credentialData);
                }
            }
        } else {
            for (IdentityCode identityCode : requiredIssuerCodes) {
                String issuerCode = identityCode.getCode();
                if (issuerCode.equals("M01") && !existingIssuerCodes.contains(issuerCode)) {
                    // 새로운 CredentialData 객체 생성
                    CredentialData credentialData = CredentialData.builder()
                                                                  .issuerCode(issuerCode)
                                                                  .issuerName(IdentityCode.fromCode(issuerCode).getDescription())
                                                                  .isIssued(UseYn.N)
                                                                  .build();
                    // credentialDataList에 추가
                    credentialDataList.add(credentialData);
                }

            }
        }
    }

    /**
     * 거래코드 생성 - 현재시간 yyyyMMddhhmmssSSS + 시큐어난수 (8자리)
     *
     * @MethodName genTrxcode
     * @return 거래코드
     */
    public static String genTrxcode() {
        Date today = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.KOREA);
        String second = secRandom(4); // 4자리 생성하고 hex code로 표현되므로 8개 자리가 나옴

        String first = formater.format(today);
        String result = first + second;

        return result;
    }

    /**
     * 난수 생성
     *
     * @MethodName SecRandom
     * @param genNum
     * @return 난수
     */
    public static String secRandom(int genNum) {
        SecureRandom random = new SecureRandom();

        byte bytes[] = new byte[genNum];

        random.nextBytes(bytes);

        return bytesToHexString(bytes);
    }

    /** Base Hex Chars */
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    /**
     * Byte Array to Hex String
     *
     * @MethodName bytesToHexString
     * @param bytes Byte Array
     * @return Hex String
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;

            hexChars[i * 2] = HEX_CHARS[value >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[value & 0x0f];
        }

        return new String(hexChars);
    }

    /**
     *
     */
    public static byte[] toBytes(String hexString) {
        if (hexString != null && hexString.length() % 2 == 0) {
            char[] hex = hexString.toCharArray();
            int length = hex.length / 2;
            byte[] raw = new byte[length];

            for (int i = 0; i < length; ++i) {
                int high = Character.digit(hex[i * 2], 16);
                int low = Character.digit(hex[i * 2 + 1], 16);
                if (high < 0 || low < 0) {
                    throw new ServiceException(ErrorCode.INVALID_PARAMETER);
                }
                int value = high << 4 | low;
                if (value > 127) {
                    value -= 256;
                }

                raw[i] = (byte) value;
            }

            return raw;
        } else {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }
    }
}
