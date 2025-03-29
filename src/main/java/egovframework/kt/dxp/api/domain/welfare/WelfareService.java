package egovframework.kt.dxp.api.domain.welfare;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.code.CodeRepository;
import egovframework.kt.dxp.api.domain.file.FileEnum.FileDivision;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.welfare.WelfareEnum.WelfareDetailDivision;
import egovframework.kt.dxp.api.domain.welfare.WelfareEnum.WelfareDivision;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareCodeResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareComboResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareDetailSearchRequest;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareDetailSearchResponse;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareListSearchRequest;
import egovframework.kt.dxp.api.domain.welfare.record.WelfareListSearchResponse;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_WLFR;
import egovframework.kt.dxp.api.entity.L_WLFR_USR;
import egovframework.kt.dxp.api.entity.M_CD;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class WelfareService {

    private static final WelfareMapper welfareMapper = WelfareMapper.INSTANCE;
    private static final FileMapper fileMapper = FileMapper.INSTANCE;

    private static final String WELFARE_BBS_DIV = "06";

    private final WelfareRepository welfareRepository;
    private final WelfareUserRepository welfareUserRepository;
    private final FileRepository fileRepository;
    private final CodeRepository codeRepository;

    private final MessageConfig messageConfig;

    @Transactional
    public GridResponse<WelfareListSearchResponse> getListSearchWelfare(WelfareListSearchRequest request) throws IOException {
        WelfareDivision welfareDivisionEnumerator = WelfareDivision.ALL;
        WelfareDetailDivision welfareDetailDivisionEnumerator = WelfareDetailDivision.ALL;
        boolean isMatchFound = false;

        // 빈값이 아닌 경우 검증을 하고 값 세팅
        if (!ObjectUtils.isEmpty(request.welfareDivision())) {
            List<M_CD> welfareDivisionCodeList = codeRepository.findByKeyGroupCodeIdOrderBySortSequenceNumberAsc("WLFR_DIV");

            // M_CD codeList를 순회하면서 일치하는 코드가 있는지 확인
            for (M_CD codeList : welfareDivisionCodeList) {
                // 비교: welfareDivisionCode와 codeList.getKey().getCodeId()가 일치하는지
                if (codeList.getKey().getCodeId().equals(request.welfareDivision())) {
                    // 일치하는 경우, welfareDivisionEnumerator 설정
                    welfareDivisionEnumerator = WelfareDivision.getEnum(request.welfareDivision());
                    isMatchFound = true;
                    break;  // 일치하는 항목을 찾았으므로 더 이상 확인할 필요 없음
                }
            }

            // 일치하는 코드가 없으면 예외 처리
            if (!isMatchFound) {
                throw new ServiceException(ErrorCode.INVALID_PARAMETER, "No matching welfare division code found: " + request.welfareDivision());
            }
        }

        isMatchFound = false;
        // 빈값이 아닌 경우 검증을 하고 값 세팅
        if (!ObjectUtils.isEmpty(request.welfareDetailDivision())) {
            switch (welfareDivisionEnumerator) {
                case LIFECYCLE -> {
                    List<M_CD> welfareDetailDivisionCodeList = codeRepository.findByKeyGroupCodeIdOrderBySortSequenceNumberAsc("LIFE_CYCLE_DIV");
                    for(M_CD codeList : welfareDetailDivisionCodeList) {
                        if (codeList.getKey().getCodeId().equals(request.welfareDetailDivision())) {
                            // 일치하는 경우, WelfareDetailDivision 설정
                            welfareDetailDivisionEnumerator = WelfareDetailDivision.DEFAULT;
                            isMatchFound = true;
                            break;  // 일치하는 항목을 찾았으므로 더 이상 확인할 필요 없음
                        }
                    }
                }
                case DISABILITY, LOW_INCOME, WOMEN_FAMILY, OTHERS -> isMatchFound = true;
            }
            // 일치하는 코드가 없으면 예외 처리
            if (!isMatchFound) {
                throw new ServiceException(ErrorCode.INVALID_PARAMETER, "No matching welfare division code found: " + request.welfareDetailDivision());
            }
        }

        Optional<Page<L_WLFR>> welfareList;
        Pageable pageable = PageRequest.of(request.pageNo(), request.pageSize());

        switch (welfareDivisionEnumerator) {
            case ALL -> welfareList = findAllWelfareList(pageable); // welfareDivision 빈값인 경우 전체 조회
            default -> {
                switch (welfareDetailDivisionEnumerator) {
                    case ALL -> welfareList = welfareRepository.findByWelfareDivisionOrderByCreateDateDesc(request.welfareDivision(), pageable); // welfareDetailDivision 빈값인 경우 해당 탭 목록 전체 조회
                    default -> welfareList = welfareRepository.findByWelfareDivisionAndWelfareDetailDivisionOrderByCreateDateDesc(request.welfareDivision(), request.welfareDetailDivision(), pageable);
                }
            }
        }

        List<L_FILE> fileList = new ArrayList<>();
        if (welfareList.isPresent() && welfareList.get().hasContent()) {
            fileList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumberInAndFileDivision(
                    WELFARE_BBS_DIV, welfareList.get().stream().map(L_WLFR::getWelfareSequenceNumber).toList(), FileDivision.MI.name());
        }

        List<WelfareListSearchResponse> responseList = new ArrayList<>();
        if (!welfareList.isEmpty()){
            responseList = welfareMapper.toSearchListResponseList(welfareList.get().toList(), fileList);
        }

        long totalSize = welfareList.map(Page::getTotalElements).orElse(0L);
        int totalPageSize = welfareList.map(Page::getTotalPages).orElse(0);
        int size = welfareList.map(Slice::getNumberOfElements).orElse(0);

        return GridResponse.<WelfareListSearchResponse>builder()
                           .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                           .totalSize(totalSize)
                           .totalPageSize(totalPageSize)
                           .size(size)
                           .items(responseList)
                           .build();
    }

    @Transactional
    public ItemsResponse<WelfareCodeResponse> getWelfareDivisionCodeList() {
        List<WelfareCodeResponse> welfareDivisionCodeList = welfareMapper.toCodeResponseList(
                codeRepository.findByKeyGroupCodeIdOrderBySortSequenceNumberAsc("WLFR_DIV"));

        return ItemsResponse.<WelfareCodeResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(welfareDivisionCodeList)
                .build();
    }

    @Transactional
    public ItemsResponse<WelfareComboResponse> getWelfareComboList() {
        // 다른 복지 구분에도 세부 분류 추가될 경우 로직 수정 필요
        List<WelfareComboResponse> comboList
                = welfareMapper.toComboResponseList(
                codeRepository.findByKeyGroupCodeIdOrderBySortSequenceNumberAsc("LIFE_CYCLE_DIV"));

        return ItemsResponse.<WelfareComboResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(comboList)
                .build();
    }

    @Transactional
    public ItemResponse<WelfareDetailSearchResponse> getSearchWelfareDetail(WelfareDetailSearchRequest request) {
        String userId = CommonUtils.getUserId();
        L_WLFR welfare
                = welfareRepository.findByWelfareSequenceNumber(request.welfareSequenceNumber())
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        boolean isDuplicated
                = welfareUserRepository.existsAllByKeyWelfareSequenceNumberAndKeyUserId(

                request.welfareSequenceNumber(), userId);
        if (!isDuplicated) {
            L_WLFR_USR welfareUserLog = L_WLFR_USR.builder()
                    .welfareSequenceNumber(request.welfareSequenceNumber())
                    .userId(userId)
                    .build();
            welfareUserRepository.saveAndFlush(welfareUserLog);
        }

        List<L_FILE> fileList =
                fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumberAndFileDivision(
                        WELFARE_BBS_DIV, request.welfareSequenceNumber(), FileDivision.C.name());

        List<L_FILE> mainImageList =
                fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumberAndFileDivision(
                        WELFARE_BBS_DIV, request.welfareSequenceNumber(), FileDivision.MI.name());

        WelfareDetailSearchResponse response = welfareMapper.toSearchDetailResponse(welfare, fileList, mainImageList);

        return ItemResponse.<WelfareDetailSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(response)
                .build();
    }

//    @Transactional
//    public void getDownloadFile(FileDownloadRequest parameter, HttpServletResponse httpServletResponse) {
//
//        if (parameter.fileSequenceNumber() == null) {
//            throw new ServiceException(ErrorCode.NO_DATA);
//        }
//        L_FILE entity = fileRepository.findById(parameter.fileSequenceNumber())
//                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
//
//        NormalFileDownloader fileDownloader = new NormalFileDownloader(httpServletResponse, entity.getActualFileName(),
//                entity.getSaveFileName(), entity.getFilePath());
//        fileDownloader.download();
//    }
//
//    // byte[] 데이터를 UTF-8 문자열로 변환
//    public static String byteArrayToString(byte[] byteArray) {
//        return new String(byteArray, StandardCharsets.UTF_8);
//    }
//
//
    public Optional<Page<L_WLFR>> findAllWelfareList(Pageable pageable) {
        // findAll()로 데이터를 조회
        Page<L_WLFR> result = welfareRepository.findAllByOrderByCreateDateDesc(pageable);

        // 데이터가 없으면 Optional.empty()를 반환, 있으면 Optional.of()로 감쌈
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
}
