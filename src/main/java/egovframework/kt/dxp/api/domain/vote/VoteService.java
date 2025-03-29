package egovframework.kt.dxp.api.domain.vote;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.file.FileMapper;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.domain.vote.record.*;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_VOTE;
import egovframework.kt.dxp.api.entity.L_VOTE_ITEM;
import egovframework.kt.dxp.api.entity.L_VOTE_USR;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import egovframework.kt.dxp.api.entity.key.L_VOTE_USR_KEY;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 투표 Service
 *
 * @author BITNA
 * @apiNote 2024-11-06 BITNA selectLimitCount -> min/maxSelectCount 컬럼변경
 * 2024-11-07 BITNA 정렬 추가
 * 2024-11-15 BITNA 목록조회 nodata error 삭제
 * 2024-11-18 BITNA 투표 결과 계산 로직 추가
 * 2024-11-19 BITNA 사용자 참여 구분 코드 변경
                 * 사용자 참여 구분 코드
                 * 01: 전체
                 * 02: 참여 가능
                 * 03: 참여(한) 완료
                 * 04: 미참여
                 * 05: 불가능
 * 2024-11-20 BITNA 상세조회 시 image 반환 추가
 * @since 2024-10-31<br />
 */
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class VoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    // progressEndDivision(투표 진행/종료 구분) - 전체[01], 진행[02], 종료[03]
    private static final String VOTE_PRGRS = "02";
    private static final String VOTE_END = "03";

    // userVoteDivision(사용자 투표 구분) - 전체[01], 투표가능[02], 투표완료[03], 투표 불가능[05]
    private static final String USER_VOTE_AVAILABLE = "02";
    private static final String USER_VOTE_UNAVAILABLE = "05";
    private static final String USER_VOTE_COMPLETED = "03";

    private static final String VOTE_BBS_DIV = "05";

    private static final VoteMapper voteMapper = VoteMapper.INSTANCE;
    private static final FileMapper fileMapper = FileMapper.INSTANCE;

    private final VoteRepository voteRepository;
    private final VoteUserRepository voteUserRepository;
    private final MessageConfig messageConfig;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    /**
     * 투표 정보 리스트 조회
     *
     * @param parameter 투표 조회 조건
     * @return VoteSearchResponse 투표 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-31<br />
     */
    @Transactional
    public ItemsResponse<VoteSearchResponse> getSearchVoteList(VoteSearchRequest parameter) {
        M_USR userInfo = userRepository.findOneByUserId(CommonUtils.getUserId()).orElseThrow(() ->
                new ServiceException(ErrorCode.NOT_AUTHENTICATION)
        );

        LocalDateTime curDateTime = Converter.getCurrentLocalDateTime();

        // 투표 마감일이 30일 지나지 않고 시작일이 현재거나 이전인 데이터만 표출
        List<L_VOTE> voteList = voteRepository.findByStartDateLessThanEqualAndEndDateAfterOrderByEndYnAscEndDateAscStartDateAsc(curDateTime, curDateTime.minusDays(30));

        // 투표여부 조회
        List<L_VOTE_USR> voteUserList = voteUserRepository.findByKeyUserId(userInfo.getUserId());

        // 투표 종료일 경우
        // progressEndDivision: 투표 진행/종료 구분 - 전체[01], 진행[02], 종료[03]
        if (parameter.progressEndDivision().equals(VOTE_PRGRS)) {
            voteList = voteList.stream().filter(entity -> UseYn.N.equals(entity.getEndYn())).toList();
        } else if (parameter.progressEndDivision().equals(VOTE_END)) {
            voteList = voteList.stream().filter(entity -> UseYn.Y.equals(entity.getEndYn())).toList();
        }

        List<VoteSearchResponse> responseList = voteList.stream().map(voteEntity -> {
            return VoteSearchResponse.builder()
                    .voteSequenceNumber(voteEntity.getVoteSequenceNumber())
                    .title(voteEntity.getTitle())
                    .contents(voteEntity.getContents())
                    .participationStartAge(voteEntity.getParticipationStartAge())
                    .participationEndAge(voteEntity.getParticipationEndAge())
                    .ageDescription(Converter.getAgeDescription(voteEntity.getParticipationStartAge(), voteEntity.getParticipationEndAge()))
                    .maleAbleYn(voteEntity.getMaleAbleYn())
                    .femaleAbleYn(voteEntity.getFemaleAbleYn())
                    .startDate(Converter.localDateTimeToString(voteEntity.getStartDate(), DateType.YYYYMMDD_FORMAT))
                    .endDate(Converter.localDateTimeToString(voteEntity.getEndDate(), DateType.YYYYMMDD_FORMAT))
                    .endYn(voteEntity.getEndYn())
                    .limitTime(Converter.getRemainingTime(voteEntity.getStartDate(), voteEntity.getEndDate()))
                    .userVoteDivision(checkVoteAvailableYn(voteEntity, voteUserList, userInfo))
                    .voteCount(voteUserRepository.getVoteCount(voteEntity.getVoteSequenceNumber()))
                    .build();
        }).toList();

        // 사용자 투표 구분 - 전체[01], 참여가능[02], 참여불가능[05], 참여완료[03]
        // 참여 가능
        if (parameter.userVoteDivision().equals(USER_VOTE_AVAILABLE)) {
            responseList = responseList.stream().filter(response -> USER_VOTE_AVAILABLE.equals(response.userVoteDivision())).toList();
            // 참여한 투표일 경우 : voteDiv가 투표완료[03]
        } else if (parameter.userVoteDivision().equals(USER_VOTE_UNAVAILABLE)) {
            responseList = responseList.stream().filter(response -> USER_VOTE_UNAVAILABLE.equals(response.userVoteDivision())).toList();
        } else if (parameter.userVoteDivision().equals(USER_VOTE_COMPLETED)) {
            responseList = responseList.stream().filter(response -> USER_VOTE_COMPLETED.equals(response.userVoteDivision())).toList();
        }

        //paging 처리
        PageRequest pageRequest = PageRequest.of(parameter.pageNo(), parameter.pageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), responseList.size());
        Page<VoteSearchResponse> responsesPage = new PageImpl<>(responseList.subList(start, end), pageRequest, responseList.size());

        List<VoteSearchResponse> pageList = new ArrayList<>(responsesPage.getContent());

        return ItemsResponse.<VoteSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .items(pageList)
                .totalSize((long) responseList.size())
                .build();
    }

    /**
     * 투표 정보 상세 조회
     *
     * @param parameter 투표 조회 조건
     * @return VoteSearchResponse 투표 조회 결과 응답 결과
     * @author BITNA
     * @since 2024-10-31<br />
     */
    @Transactional
    public ItemResponse<VoteDetailSearchResponse> getSearchVoteDetail(VoteDetailSearchRequest parameter) {
        M_USR userInfo = userRepository.findOneByUserId(CommonUtils.getUserId()).orElseThrow(() ->
                new ServiceException(ErrorCode.NOT_AUTHENTICATION)
        );

        // 투표 조회
        L_VOTE voteEntity = voteRepository.findById(parameter.voteSequenceNumber()).orElseThrow(() ->
                new ServiceException(ErrorCode.INVALID_PARAMETER)
        );

        // 투표여부 조회
        List<L_VOTE_USR> voteUserList = voteUserRepository.findByKeyUserIdAndKeyVoteSequenceNumber(userInfo.getUserId(), voteEntity.getVoteSequenceNumber());
        List<Integer> itemSequenceNumbers = voteUserList.stream().map(entity -> entity.getKey().getItemSequenceNumber()).toList();

        // 투표항목 조회
        List<VoteItemSearchResponse> itemList = voteMapper.toVoteItemSearchResponseList(voteEntity.getVoteItemList().stream()
                .sorted(Comparator.comparing(L_VOTE_ITEM::getSortSequenceNumber)).toList());

        String voteDiv = checkVoteAvailableYn(voteEntity, voteUserList, userInfo);

        // TODO: 투표 완료인 경우 내가 투표한 항목 표시(response에 추가)
        if (USER_VOTE_COMPLETED.equals(voteDiv)) {
            itemList = itemList.stream().map(item -> {
                if (itemSequenceNumbers.contains(item.itemSequenceNumber())) {
                    return item.setItemSelectYn(UseYn.Y);
                } else {
                    return item.setItemSelectYn(UseYn.N);
                }
            }).toList();
        }

        // TODO: 투표 종료인 경우 결과 표시
        if (UseYn.Y.equals(voteEntity.getEndYn())) {
            List<L_VOTE_USR_KEY> itemUserKeyList = voteUserRepository.findByKeyVoteSequenceNumber(parameter.voteSequenceNumber()).stream().map(L_VOTE_USR::getKey).toList();
            int totalCount = itemUserKeyList.size();
            Map<Integer, List<L_VOTE_USR_KEY>> groupingItem = itemUserKeyList.stream().collect(Collectors.groupingBy(L_VOTE_USR_KEY::getItemSequenceNumber));
            itemList = itemList.stream().map(item -> {
                if (groupingItem.get(item.itemSequenceNumber()) != null && groupingItem.get(item.itemSequenceNumber()).size() > 0) {
                    Integer selectCount = groupingItem.get(item.itemSequenceNumber()).size();
                    Double itemSelectedRate = (double) selectCount / totalCount * 100;

                    return item.setSelectStatistics(selectCount, String.format("%.2f", itemSelectedRate));
                }
                return item.setSelectStatistics(0, "0");
            }).toList();
        }

        List<L_FILE> fileList =
                fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(
                        VOTE_BBS_DIV, parameter.voteSequenceNumber());

        VoteDetailSearchResponse response = VoteDetailSearchResponse.builder()
                .voteSequenceNumber(voteEntity.getVoteSequenceNumber())
                .title(voteEntity.getTitle())
                .contents(voteEntity.getContents())
                .minSelectCount(voteEntity.getMinSelectCount())
                .maxSelectCount(voteEntity.getMaxSelectCount())
                .participationStartAge(voteEntity.getParticipationStartAge())
                .participationEndAge(voteEntity.getParticipationEndAge())
                .ageDescription(Converter.getAgeDescription(voteEntity.getParticipationStartAge(), voteEntity.getParticipationEndAge()))
                .maleAbleYn(voteEntity.getMaleAbleYn())
                .femaleAbleYn(voteEntity.getFemaleAbleYn())
                .startDate(Converter.localDateTimeToString(voteEntity.getStartDate(), DateType.YYYYMMDD_FORMAT))
                .endDate(Converter.localDateTimeToString(voteEntity.getEndDate(), DateType.YYYYMMDD_FORMAT))
                .endYn(voteEntity.getEndYn())
                .limitTime(Converter.getRemainingTime(voteEntity.getStartDate(), voteEntity.getEndDate()))
                .voteDiv(checkVoteAvailableYn(voteEntity, voteUserList, userInfo))
                .voteCount(voteUserRepository.getVoteCount(voteEntity.getVoteSequenceNumber()))
                .fileList(fileMapper.toSearchResponseList(fileList))
                .itemList(itemList)
                .build();

        return ItemResponse.<VoteDetailSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(response)
                .build();
    }

    // 투표 가능 여부
    private String checkVoteAvailableYn(L_VOTE voteEntity, List<L_VOTE_USR> voteUserList, M_USR userInfo) {
        LocalDateTime curTime = Converter.getCurrentLocalDateTime();

        //투표 구분(투표가능[02], 투표불가능[03], 투표완료[04])
        String voteDiv = USER_VOTE_UNAVAILABLE;

        // 투표날짜 및 종료 여부 체크
        if (voteEntity.getStartDate().isBefore(curTime) && voteEntity.getEndDate().isAfter(curTime) && voteEntity.getEndYn() != UseYn.Y) {
            String userGender = userInfo.getGenderType();
            Integer userAge = CommonUtils.calculateAge(Objects.requireNonNull(Converter.stringToLocalDateTime(userInfo.getBirthDate() + "000000", DateType.YYYYMMDDHHMMSS)));

            // 투표 자격 체크(성별, 나이)
            if (((UseYn.Y.equals(voteEntity.getMaleAbleYn()) && "M".equals(userGender))
                    || (UseYn.Y.equals(voteEntity.getFemaleAbleYn()) && "F".equals(userGender)))
                    && (CommonUtils.isAgeInRange(voteEntity.getParticipationStartAge(), voteEntity.getParticipationEndAge(), userAge))) {
                // 투표 가능
                voteDiv = USER_VOTE_AVAILABLE;
            }
        }

        List<Integer> voteSqnoList = voteUserList.stream().map(entity -> entity.getKey().getVoteSequenceNumber()).toList();
        // 투표했는지 체크
        if (voteSqnoList.contains(voteEntity.getVoteSequenceNumber())) {
            // 투표 완료
            voteDiv = USER_VOTE_COMPLETED;
        }
        return voteDiv;
    }

    /**
     * 투표 정보 추가
     *
     * @param parameter 투표 추가 요청 정보
     * @return VoteCreateResponse 생성 된 투표 정보 응답
     * @author BITNA
     * @since 2024-10-31<br />
     */
    @Transactional
    public ItemResponse<Long> createVoteUser(VoteUserCreateRequest parameter) {

        Integer voteSequenceNumber = parameter.voteSequenceNumber();
        List<Integer> itemList = parameter.itemSequenceNumberList();
        M_USR userInfo = userRepository.findOneByUserId(CommonUtils.getUserId()).orElseThrow(() ->
                new ServiceException(ErrorCode.NOT_AUTHENTICATION)
        );

        // 투표 조회
        L_VOTE voteEntity = voteRepository.findById(parameter.voteSequenceNumber()).orElseThrow(() ->
                new ServiceException(ErrorCode.INVALID_PARAMETER)
        );

        // 투표여부 조회
        List<L_VOTE_USR> voteUserList = voteUserRepository.findByKeyUserIdAndKeyVoteSequenceNumber(userInfo.getUserId(), voteEntity.getVoteSequenceNumber());

        List<Integer> voteItemList = voteEntity.getVoteItemList().stream().map(entity -> entity.getKey().getItemSequenceNumber()).toList();

        // 선택 제한 수 검사 및 항목 체크
        if (voteEntity.getMinSelectCount() <= itemList.size() && voteEntity.getMaxSelectCount() >= itemList.size() && new HashSet<>(voteItemList).containsAll(itemList)) {
            if (USER_VOTE_AVAILABLE.equals(checkVoteAvailableYn(voteEntity, voteUserList, userInfo))) {
                List<L_VOTE_USR> saveVoteUserList = itemList.stream().map(item -> {
                    return L_VOTE_USR.builder()
                            .voteSequenceNumber(voteSequenceNumber)
                            .itemSequenceNumber(item)
                            .userId(userInfo.getUserId())
                            .build();
                }).toList();

                voteUserRepository.saveAll(saveVoteUserList);
            } else {
                throw new ServiceException(ErrorCode.INVALID_PARAMETER);
            }
        } else {
            LOGGER.error("Invalid Data. minSelectCount: {}, maxSelectCount: {}, items: {}, UserSelectedItemList: {}",
                    voteEntity.getMinSelectCount(), voteEntity.getMaxSelectCount(), voteItemList.toString(), itemList.toString());
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        return ItemResponse.<Long>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item((long) 1)
                .build();
    }
}
