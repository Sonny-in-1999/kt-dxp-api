package egovframework.kt.dxp.api.domain.terms;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.domain.code.CodeRepository;
import egovframework.kt.dxp.api.domain.terms.record.*;
import egovframework.kt.dxp.api.entity.L_TRMS;
import egovframework.kt.dxp.api.entity.M_CD;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 약관 Service
 *
 * @author BITNA
 * @apiNote 2024-11-15 BITNA 목록조회 nodata error 삭제
 * @since 2024-10-18<br />
 */
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class TermsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TermsService.class);

    private final TermsRepository termsRepository;
    private static final TermsMapper termsMapper = TermsMapper.INSTANCE;
    private final CodeRepository codeRepository;
    private final MessageConfig messageConfig;

    /**
     * 약관 정보 유형 조회
     *
     * @param 약관 조회 조건
     * @return TermsSearchResponse 약관 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemsResponse<TermsGroupSearchResponse> getTermsTypeList() {
        /*queryMethod or querydsl or namedNativeQuery 작성*/
        List<M_CD> mCdList = codeRepository.findByKeyGroupCodeIdAndUseYnOrderBySortSequenceNumberAsc("TRMS_TYPE", UseYn.Y);

        return ItemsResponse.<TermsGroupSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(termsMapper.toGroupSearchResponseList(mCdList))
                .build();
    }

    /**
     * 약관 정보 날짜 리스트 조회
     *
     * @param 약관 조회 조건
     * @return TermsSearchResponse 약관 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemResponse<TermsStartDateSearchResponse> getTermsStartDateList(TermsStartDateSearchRequest parameter) {
        /*queryMethod or querydsl or namedNativeQuery 작성*/
        List<L_TRMS> termsList = termsRepository.findByKeyTermsTypeAndKeyTermsStartDateLessThanEqualAndUseYnOrderByKeyTermsStartDateDesc(parameter.termsType(),
                Converter.getCurrentLocalDateTime(), UseYn.Y);

        List<String> termsStartDateList = termsList.stream()
                .map(entity -> Converter.localDateTimeToString(entity.getKey().getTermsStartDate(), DateType.YYYYMMDD_FORMAT)).toList();

        TermsStartDateSearchResponse response = TermsStartDateSearchResponse.builder()
                .termsType(parameter.termsType())
                .termsStartDateList(termsStartDateList)
                .build();

        return ItemResponse.<TermsStartDateSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(response)
                .build();
    }

    /**
     * 약관 정보 조회
     *
     * @param 약관 조회 조건
     * @return TermsSearchResponse 약관 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-18<br />
     */
    @Transactional
    public ItemResponse<TermsSearchResponse> getTerms(TermsSearchRequest parameter) {

        String dateTime = parameter.termsStartDate() + " 00:00:00";
        /*queryMethod or querydsl or namedNativeQuery 작성*/
        Optional<L_TRMS> terms = termsRepository.findByKeyTermsTypeAndKeyTermsStartDateAndUseYn(parameter.termsType(), Converter.stringToLocalDateTime(dateTime, DateType.YYYYMMDDHHMMSS_FORMAT), UseYn.Y);

        if (terms.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_DATA);
        }

        return ItemResponse.<TermsSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(termsMapper.toSearchResponse(terms.get()))
                .build();
    }

    /**
     * 최신 약관 정보 조회
     *
     * @param 최신 약관 조회 조건
     * @return TermsSearchResponse 약관 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-11-05<br />
     */
    @Transactional
    public ItemResponse<TermsSearchResponse> getRecentTerms(TermsSearchRequest parameter) {
        /*queryMethod or querydsl or namedNativeQuery 작성*/
        L_TRMS termsEntity = termsRepository.findTopByKeyTermsTypeAndKeyTermsStartDateLessThanEqualAndUseYnOrderByKeyTermsStartDateDesc(parameter.termsType(), Converter.getCurrentLocalDateTime(), UseYn.Y).orElseThrow(() ->
                new ServiceException(ErrorCode.NO_DATA)
        );

        return ItemResponse.<TermsSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(termsMapper.toSearchResponse(termsEntity))
                .build();
    }
}
