package egovframework.kt.dxp.api.domain.participation;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.GridResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.participation.ParticipationEnum.DivisionCode;
import egovframework.kt.dxp.api.domain.participation.ParticipationEnum.SearchDivision;
import egovframework.kt.dxp.api.domain.participation.ParticipationEnum.SearchSubDivision;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationCompletedStatusImpl;
import egovframework.kt.dxp.api.domain.participation.model.ParticipationListImpl;
import egovframework.kt.dxp.api.domain.participation.record.ParticipationSearchRequest;
import egovframework.kt.dxp.api.domain.participation.record.ParticipationSearchResponse;
import egovframework.kt.dxp.api.domain.proposal.ProposalRepository;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyUserRepository;
import egovframework.kt.dxp.api.domain.vote.VoteRepository;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import io.swagger.annotations.Api;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "[CDA-SER] 참여내역", description = "[담당자 : Juyoung Chae]")
@RequiredArgsConstructor
public class ParticipationService {
    private static final Integer LIST_DISPLAY_PERIOD = 90;
    private final MessageConfig messageConfig;
    private final ProposalRepository proposalRepository;
    private final VoteRepository voteRepository;
    private final SurveyUserRepository surveyUserRepository;
    // 제안 상태
    private static final String PRPS_DIV_SUBMITTED  = "01";  // 제안 접수
    private static final String PRPS_DIV_COMPLETED  = "02";  // 검토 완료

    private UseYn hasRedBadgeProposal;
    private UseYn hasRedBadgeVote;
    private UseYn hasRedBadgeSurvey;

    @Transactional
    public GridResponse<ParticipationSearchResponse> getParticipationList(ParticipationSearchRequest parameter) {
        SearchDivision    searchDivision;
        SearchSubDivision searchSubDivision;
        UseYn isCompleted = null;
        List<String> prgrsDivList = null;
        hasRedBadgeProposal = UseYn.N;
        hasRedBadgeVote     = UseYn.N;
        hasRedBadgeSurvey   = UseYn.N;

        // 유효한 값인지 먼저 확인
        if (SearchDivision.isValid(parameter.searchDivision()) && SearchSubDivision.isValid(parameter.searchSubDivision())) {
            searchDivision    = SearchDivision.valueOf(parameter.searchDivision());
            searchSubDivision = SearchSubDivision.valueOf(parameter.searchSubDivision());
        } else {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        // 페이지 번호가 음수이거나 사이즈가 음수인 경우 예외 처리
        if (parameter.pageNo() < 0 || parameter.pageSize() <= 0) {
            throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        switch (searchSubDivision) {
            case ALL:
                break;
            case PROGRESS:
                prgrsDivList = List.of(PRPS_DIV_SUBMITTED);
                isCompleted  = UseYn.N;
                break;
            case COMPLETE:
                prgrsDivList = List.of(PRPS_DIV_COMPLETED);
                isCompleted  = UseYn.Y;
                break;
            default:
                throw new ServiceException(ErrorCode.INVALID_PARAMETER);
        }

        Page<ParticipationListImpl> page = this.getMyParticipation(searchDivision, prgrsDivList, isCompleted, parameter.pageNo(), parameter.pageSize());

        ParticipationSearchResponse participationSearchResponse = ParticipationSearchResponse.builder()
                                                                                             .hasRedBadgeProposal(hasRedBadgeProposal)
                                                                                             .hasRedBadgeVote(hasRedBadgeVote)
                                                                                             .hasRedBadgeSurvey(hasRedBadgeSurvey)
                                                                                             .list(page.getContent())
                                                                                             .build();

        List<ParticipationSearchResponse> participationSearchResponseArrayList = new ArrayList<>();
        participationSearchResponseArrayList.add(participationSearchResponse);
        return GridResponse.<ParticipationSearchResponse>builder()
                           .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                           .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                           .totalSize(page.getTotalElements())
                           .totalPageSize(page.getTotalPages())
                           .size(page.getNumberOfElements())
                           .items(participationSearchResponseArrayList)
                           .build();
    }

    private Page<ParticipationListImpl> getMyParticipation(SearchDivision searchDivision, List<String> prgrsDivList, UseYn isCompleted, int page, int size) {
        return executeServiceIfNeeded(searchDivision, prgrsDivList, isCompleted, page, size);
    }

    public void updateRedBadgeStatus(ParticipationCompletedStatusImpl participationCompletedList) {
        // 각 DivisionCode에 대해 RedBadge 상태 업데이트
        setRedBadgeForDivision(participationCompletedList, DivisionCode.PROPOSAL, () -> hasRedBadgeProposal = UseYn.Y);
        setRedBadgeForDivision(participationCompletedList, DivisionCode.VOTE, () -> hasRedBadgeVote = UseYn.Y);
        setRedBadgeForDivision(participationCompletedList, DivisionCode.SURVEY, () -> hasRedBadgeSurvey = UseYn.Y);
    }

    // 공통 메서드로 RedBadge 설정
    private void setRedBadgeForDivision(ParticipationCompletedStatusImpl participationCompletedList, DivisionCode divisionCode, Runnable action) {
        if (divisionCode.getValue() == participationCompletedList.getDivisionCode() && UseYn.Y.equals(participationCompletedList.getHasRedBadge())) {
            action.run();  // 조건이 맞으면 해당 동작 실행
        }
    }

    // 서비스 실행 조건을 체크하는 메서드
    public Page<ParticipationListImpl> executeServiceIfNeeded(SearchDivision searchDivision, List<String> prgrsDivList, UseYn isCompleted, int page, int size) {
        String userId = CommonUtils.getUserId();
        // 90일 정책 미정 -> 확정되면 변경 필요
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(LIST_DISPLAY_PERIOD);
        Pageable pageable = PageRequest.of(page, size);

        List<ParticipationCompletedStatusImpl> participationCompletedStatus = proposalRepository.findServiceResultAndCompletedStatus(userId, daysAgo);
        for (ParticipationCompletedStatusImpl participationCompletedList : participationCompletedStatus) {
                updateRedBadgeStatus(participationCompletedList);
        }

        return switch (searchDivision) {
            case ALL -> proposalRepository.findAllParticipationListBeforeDate(userId, daysAgo, pageable);
            case PROPOSAL ->
                    proposalRepository.findParticipationProposalListBeforeDate(userId, daysAgo, (prgrsDivList == null) ? null : prgrsDivList.get(0), prgrsDivList, pageable);
            case VOTE ->
                    voteRepository.findParticipationVoteListBeforeDate(userId, daysAgo, (isCompleted == null) ? null : isCompleted.toString(), pageable);
            case SURVEY ->
                    surveyUserRepository.findParticipationSurveyListBeforeDate(userId, daysAgo, (isCompleted == null) ? null : isCompleted.toString(), pageable);
        };
    }
}

