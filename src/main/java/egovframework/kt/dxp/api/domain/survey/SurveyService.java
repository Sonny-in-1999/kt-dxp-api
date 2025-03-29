package egovframework.kt.dxp.api.domain.survey;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.converter.Converter;
import egovframework.kt.dxp.api.common.converter.enumeration.DateType;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.file.FileRepository;
import egovframework.kt.dxp.api.domain.survey.mapper.SurveyMapper;
import egovframework.kt.dxp.api.domain.survey.record.AnswerSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.QuestionCreateRequest;
import egovframework.kt.dxp.api.domain.survey.record.QuestionItemCreateRequest;
import egovframework.kt.dxp.api.domain.survey.record.QuestionItemSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.QuestionSearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.QuestionSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveyDetailSearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.SurveyDetailSearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveySearchRequest;
import egovframework.kt.dxp.api.domain.survey.record.SurveySearchResponse;
import egovframework.kt.dxp.api.domain.survey.record.SurveyUserCreateRequest;
import egovframework.kt.dxp.api.domain.survey.record.TotalSurveySearchResponse;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyRepository;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyUserRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.entity.L_FILE;
import egovframework.kt.dxp.api.entity.L_SURV;
import egovframework.kt.dxp.api.entity.L_SURV_USR;
import egovframework.kt.dxp.api.entity.M_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
@Slf4j
public class SurveyService {

    // surveyTabDivisionCode(설문 전체/진행/종료 구분) - 전체[01], 진행[02], 종료[03]
    private static final String SURVEY_TOTAL = "01";
    private static final String SURVEY_PRGRS = "02";
    private static final String SURVEY_END = "03";

    // surveySubDivisionCode(사용자 설문 구분) - 전체[01], 참여가능[02], 참여한 설문[03], 미참여[04]
    private static final String USER_SURVEY_TOTAL = "01";
    private static final String USER_SURVEY_AVAILABLE = "02";
    private static final String USER_SURVEY_PARTICIPATED = "03";
    private static final String USER_SURVEY_NOT_PARTICIPATION = "04";
    private static final String USER_SURVEY_UNAVAILABLE = "05";

    private static final String SURVEY_BBS_DIV = "03";

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyUserRepository surveyUserRepository;
    private final FileRepository fileRepository;

    private static final SurveyMapper surveyMapper = SurveyMapper.INSTANCE;
    private final MessageConfig messageConfig;

    @Transactional
    public GridResponse<TotalSurveySearchResponse> getSearchSurveyList(
            SurveySearchRequest request) {

        String userId = CommonUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        Page<L_SURV> list = null;

        Pageable pageable = PageRequest.of(request.pageNo(), request.pageSize());

        List<L_SURV_USR> surveyUserList = surveyUserRepository.findByKeyUserId(userId);

        M_USR userInfo = userRepository.findById(CommonUtils.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        switch (request.surveySubDivisionCode()) {
            case USER_SURVEY_TOTAL -> list = getTabDivision(request, now, pageable);
            case USER_SURVEY_AVAILABLE -> {
                // 참여가능 설문 조회
                Integer userAge = CommonUtils.calculateAge(
                        Objects.requireNonNull(
                                Converter.stringToLocalDateTime(userInfo.getBirthDate() + "000000",
                                        DateType.YYYYMMDDHHMMSS)));
                String userGender = userInfo.getGenderType();

                //if (request.surveyTabDivisionCode().equals(SURVEY_TOTAL)) {
                //    list = surveyRepository.findByOrderByEndDateAscStartDateAsc(pageable);
                //}
                //if (request.surveyTabDivisionCode().equals(SURVEY_PRGRS)) {
                //    list = surveyRepository.findByEndYnOrderByStartDateAsc(UseYn.N, pageable);
                //}

                if (userGender.equals("M")) {
                    list = surveyRepository.findByMaleParticipationEnabled(UseYn.N, userAge, UseYn.Y, userId, pageable);
                } else if (userGender.equals("F")) {
                    list = surveyRepository.findByFemaleParticipationEnabled(UseYn.N, userAge, UseYn.Y, userId, pageable);
                }

            }

            case USER_SURVEY_PARTICIPATED -> {
                // 참여한 설문 조회
                if (request.surveyTabDivisionCode().equals(SURVEY_TOTAL)) {
                    list = surveyRepository.totalParticipatedSurvey(userId, pageable);
                }
                if (request.surveyTabDivisionCode().equals(SURVEY_PRGRS)) {
                    list = surveyRepository.inProgressParticipatedSurvey(userId, now, now, pageable);
                }
                if (request.surveyTabDivisionCode().equals(SURVEY_END)) {
                    list = surveyRepository.terminatedProgressParticipatedSurvey(userId, now, pageable);
                }
            }
            case USER_SURVEY_NOT_PARTICIPATION -> {
                // 미참여
                if (request.surveyTabDivisionCode().equals(SURVEY_TOTAL)) {
                    list = surveyRepository.notParticipatedSurvey(userId, pageable);
                }
                if (request.surveyTabDivisionCode().equals(SURVEY_END)) {
                    list = surveyRepository.terminatedNotParticipatedSurvey(userId, now, pageable);
                }
            }

        }

        // TODO BO에 종료여부 UPDATE 생기면 삭제하기
        //list.forEach(surveyEntity -> {
        //    if (LocalDateTime.now().isAfter(surveyEntity.getEndDate())) {
        //        surveyEntity.setEndYn(UseYn.Y);
        //    }
        //});

        List<SurveySearchResponse> responseList = list.stream().map(surveyEntity -> SurveySearchResponse.builder()
                .surveySequenceNumber(surveyEntity.getSurveySequenceNumber())
                .userSurveyDivision(
                        checkSurveyAvailableYn(surveyEntity, surveyUserList, userInfo))
                .title(surveyEntity.getTitle())
                .startDate(Converter.localDateTimeToString(surveyEntity.getStartDate(),
                        DateType.YYYYMMDD_FORMAT))
                .endDate(Converter.localDateTimeToString(surveyEntity.getEndDate(),
                        DateType.YYYYMMDD_FORMAT))
                .limitTime(Converter.getRemainingTime(surveyEntity.getStartDate(), surveyEntity.getEndDate()))
                .participationStartAge(surveyEntity.getParticipationStartAge())
                .participationEndAge(surveyEntity.getParticipationEndAge())
                .ageDescription(Converter.getAgeDescription(surveyEntity.getParticipationStartAge(), surveyEntity.getParticipationEndAge()))
                .maleAbleYn(surveyEntity.getMaleAbleYn().toString())
                .femaleAbleYn(surveyEntity.getFemaleAbleYn().toString())
                .userParticipationCount(surveyUserRepository.getSurveyCount(surveyEntity.getSurveySequenceNumber()))
                .build()).toList();

        List<TotalSurveySearchResponse> totalList = new ArrayList<>();
        TotalSurveySearchResponse totalSurveySearchResponse = new TotalSurveySearchResponse(
                list.getNumberOfElements(),
                responseList);
        totalList.add(totalSurveySearchResponse);

        return GridResponse.<TotalSurveySearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .totalSize(list.getTotalElements())
                .totalPageSize(list.getTotalPages())
                .size(list.getNumberOfElements())
                .items(totalList)
                .build();
    }

    private Page<L_SURV> getTabDivision(SurveySearchRequest request, LocalDateTime now, Pageable pageable) {
        return switch (request.surveyTabDivisionCode()) {
            case SURVEY_TOTAL -> surveyRepository.findByOrderByEndDateAscStartDateAsc(pageable);
            case SURVEY_PRGRS -> surveyRepository.findByEndYnOrderByStartDateAsc(UseYn.N, pageable);
            case SURVEY_END -> surveyRepository.findByEndYnAndEndDateAfterOrderByEndDateAscStartDateAsc(UseYn.Y, now.minusDays(90), pageable);
            default -> throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        };
    }

    @Transactional
    public ItemResponse<SurveyDetailSearchResponse> getDetailSearchSurvey(
            SurveyDetailSearchRequest request) {

        // 설문 조회
        L_SURV surveyEntity = surveyRepository.findById(request.surveySequenceNumber())
                .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_PARAMETER));

        List<L_SURV_USR> surveyUsers = surveyUserRepository.findByKeySurveySequenceNumber(surveyEntity.getSurveySequenceNumber());

        List<L_FILE> fileList =
                fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(
                        SURVEY_BBS_DIV, request.surveySequenceNumber());

        SurveyDetailSearchResponse surveyResponse = surveyMapper.toSearchResponse(surveyEntity, fileList);

        Map<Integer, List<L_SURV_USR>> questionGroupedMap = surveyUsers.stream()
                .collect(Collectors.groupingBy(surveyUser -> surveyUser.getKey().getQuestionSequenceNumber()));

        List<QuestionItemSearchResponse> surveyQuestionResponse = surveyResponse.questionList().stream().map(question -> {
            List<QuestionSearchResponse> itemList = question.itemList();
            if (questionGroupedMap.get(question.questionSequenceNumber()) != null && !questionGroupedMap.get(question.questionSequenceNumber()).isEmpty()) {
                // 항목 목록 가져오기
                List<L_SURV_USR> questionParticipatedUserList = questionGroupedMap.get(question.questionSequenceNumber());
                int questionParticipatedUserSize = questionParticipatedUserList.size();

                //질문의 항목별로 Group by
                Map<Integer, List<L_SURV_USR>> itemGroupedMap = questionParticipatedUserList.stream()
                        .collect(Collectors.groupingBy(surveyUser -> surveyUser.getKey().getItemSequenceNumber()));

                itemList = question.itemList().stream().map(item -> {
                    int selectedCount = 0;
                    double selectedRate = 0;
                    if (itemGroupedMap.get(item.itemSequenceNumber()) != null && !itemGroupedMap.get(item.itemSequenceNumber()).isEmpty()) {
                        selectedCount = itemGroupedMap.get(item.itemSequenceNumber()).size();
                        selectedRate = (double) selectedCount / questionParticipatedUserSize * 100;
                    }
                    return item.setSelectStatistics(selectedCount, String.format("%.2f", selectedRate));
                }).toList();
            }
            return question.setItemList(itemList);
        }).toList();

        surveyResponse = surveyResponse.setQuestionList(surveyQuestionResponse);
        surveyResponse = surveyResponse.setUserParticipationCount(surveyUserRepository.getSurveyCount(surveyEntity.getSurveySequenceNumber()));

        String userId = CommonUtils.getUserId();
        List<L_SURV_USR> surveyUserList = surveyUserRepository.findByKeyUserId(userId);

        M_USR userInfo = userRepository.findById(CommonUtils.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));
        surveyResponse = surveyResponse.setUserDivisionCode(checkSurveyAvailableYn(surveyEntity, surveyUserList, userInfo));

        return ItemResponse.<SurveyDetailSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(surveyResponse)
                .build();
    }

//    public ItemsResponse<SurveyFileSearchResponse> getImageFile(
//            SurveyDetailSearchRequest request) {
//        List<L_FILE> fileEntityList = fileRepository.findByBulletinBoardDivisionAndBulletinBoardSequenceNumber(
//                SURVEY_BBS_DIV, request.surveySequenceNumber());
//
//        List<SurveyFileSearchResponse> fileSearchResponseList = fileEntityList.stream()
//                .map(entity -> {
//                    return SurveyFileSearchResponse.builder()
//                            .fileName(entity.getActualFileName())
//                            .fileExtension(entity.getFileExtension())
//                            .image(getFileStr(entity))
//                            .build();
//                }).toList();
//
//        return ItemsResponse.<SurveyFileSearchResponse>builder()
//                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
//                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
//                .items(fileSearchResponseList)
//                .build();
//    }

//    // file -> base64
//    private String getFileStr(L_FILE entity) {
//        String os = System.getProperty("os.name").toLowerCase();
//        String path = entity.getFilePath();
//
//        Path saveFilePath = null;
//        if (os.contains("linux")) {
//            saveFilePath = Path.of(File.separator + path);
//        } else if (os.contains("win")) {
//            saveFilePath = Path.of(path);
//        } else { //linux도 윈도우도 아닐경우. 그냥 상대경로로 저장.
//            saveFilePath = Path.of(path);
//        }
//
//        byte[] bytes = null;
//        try {
//            bytes = FileUtils.readFileToByteArray(new File(saveFilePath.toString()));
//        } catch (FileNotFoundException e) {
////            LOGGER.error("[N] file not found"); //file을 찾지 못하면 조용히 로그만 남기고 프론트에서 엑박.
//        } catch (IOException e) {
//            throw new ServiceException(ErrorCode.SERVICE_ERROR);
//        }
//        return CommonUtils.encodBase64(bytes);
//    }

    public GridResponse<AnswerSearchResponse> getAnswerSearchSurvey(
            QuestionSearchRequest request) {
        Pageable pageable = PageRequest.of(request.pageNo(), request.pageSize());

        Page<L_SURV_USR> page = surveyUserRepository.findByKeySurveySequenceNumberAndKeyQuestionSequenceNumberOrderByCreateDateAsc(
                request.surveySequenceNumber(), request.questionSequenceNumber(), pageable);

        List<AnswerSearchResponse> list = surveyMapper.toSearchResponseList(page.getContent());

        return GridResponse.<AnswerSearchResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .totalSize(page.getTotalElements())
                .items(list)
                .build();
    }

    @Transactional
    public ItemResponse<Long> createSurveyUser(SurveyUserCreateRequest request) {

        M_USR userInfo = userRepository.findById(CommonUtils.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.NO_DATA));

        //설문 조회
        L_SURV surveyEntity = surveyRepository.findById(request.surveySequenceNumber())
                .orElseThrow(() ->
                        new ServiceException(ErrorCode.NO_DATA));

        //설문 여부 조회
        List<L_SURV_USR> surveyUserList = surveyUserRepository.findByKeyUserIdAndKeySurveySequenceNumber(
                userInfo.getUserId(), surveyEntity.getSurveySequenceNumber());

        //참여 가능 대상인지 확인
        if (USER_SURVEY_AVAILABLE.equals(
                checkSurveyAvailableYn(surveyEntity, surveyUserList, userInfo))) {
            //선택 제한 수 검사 및 항목 체크
            surveyEntity.getSurveyQuestionList().forEach(questionEntity -> {

                List<QuestionCreateRequest> questionList = request.questionSequenceNumberList();

                // 질문에 대한 아이템 항목 순번 list
                List<Integer> questionItems = questionEntity.getSurveyQuestionItemList().stream()
                        .map(entity -> entity.getKey().getItemSequenceNumber()).toList();

                //질문 및 항목 매핑
                for (QuestionCreateRequest questionCreateRequest : questionList) {
                    if (Objects.equals(questionEntity.getKey().getQuestionSequenceNumber(),
                            questionCreateRequest.questionSequenceNumber())) {
                        List<Integer> surveyItemRequestList = questionCreateRequest
                                .itemSequenceNumberList()
                                .stream().map(QuestionItemCreateRequest::itemSequenceNumber)
                                .toList();

                        if (questionEntity.getMinSelectCount() <= surveyItemRequestList.size()
                                && questionEntity.getMaxSelectCount()
                                >= surveyItemRequestList.size()
                                && new HashSet<>(questionItems).containsAll(
                                surveyItemRequestList)) {

                            Integer questionSequenceNumber = questionCreateRequest
                                    .questionSequenceNumber();
                            List<L_SURV_USR> saveSurveyUserList = questionCreateRequest
                                    .itemSequenceNumberList().stream()
                                    .map(item -> {
                                        return L_SURV_USR.builder()
                                                .surveySequenceNumber(
                                                        request.surveySequenceNumber())
                                                .questionSequenceNumber(questionSequenceNumber)
                                                .itemSequenceNumber(item.itemSequenceNumber())
                                                .userId(userInfo.getUserId())
                                                .answer(item.answer())
                                                .build();
                                    }).toList();

                            surveyUserRepository.saveAll(saveSurveyUserList);

                        } else {
                            log.error(
                                    "Invalid Data: minSelectCount: {}, maxSelectCount: {}, items: {}",
                                    questionEntity.getMinSelectCount(),
                                    questionEntity.getMaxSelectCount(),
                                    surveyItemRequestList);
                            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
                        }
                    }
                }

            });
        } else {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }
        return ItemResponse.<Long>builder()
                .status(messageConfig.getCode(NormalCode.CREATE_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.CREATE_SUCCESS))
                .item(1L)
                .build();

    }

    //참여가능 여부
    private String checkSurveyAvailableYn(L_SURV surveyEntity, List<L_SURV_USR> surveyUserList,
                                          M_USR userInfo) {
        LocalDateTime curTime = Converter.getCurrentLocalDateTime();

        //설문 구분(참여가능[02], 참여불가능[05])
        String surveyDiv = USER_SURVEY_UNAVAILABLE;

        // 설문 날짜 및 종료 여부 체크
        if (surveyEntity.getStartDate().isBefore(curTime) && surveyEntity.getEndDate()
                .isAfter(curTime) && surveyEntity.getEndYn() != UseYn.Y) {
            String userGender = userInfo.getGenderType();
            Integer userAge = CommonUtils.calculateAge(
                    Objects.requireNonNull(
                            Converter.stringToLocalDateTime(userInfo.getBirthDate() + "000000",
                                    DateType.YYYYMMDDHHMMSS)));
            // 설문 자격 체크(성별, 나이)
            if (((UseYn.Y.equals(surveyEntity.getMaleAbleYn()) && "M".equals(userGender))
                    || (UseYn.Y.equals(surveyEntity.getFemaleAbleYn()) && "F".equals(userGender)))
                    && (CommonUtils.isAgeInRange(surveyEntity.getParticipationStartAge(), surveyEntity.getParticipationEndAge(), userAge))) {
                surveyDiv = USER_SURVEY_AVAILABLE;

            }
        }
        List<Integer> voteSqnoList = surveyUserList.stream()
                .map(entity -> entity.getKey().getSurveySequenceNumber()).toList();
        // 참여했는지 체크
        if (voteSqnoList.contains(surveyEntity.getSurveySequenceNumber())) {
            surveyDiv = USER_SURVEY_PARTICIPATED;
        }
        return surveyDiv;
    }

}