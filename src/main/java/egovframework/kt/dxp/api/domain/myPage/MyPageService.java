package egovframework.kt.dxp.api.domain.myPage;

import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.util.CommonUtils;
import egovframework.kt.dxp.api.domain.myPage.record.MyPageResponse;
import egovframework.kt.dxp.api.domain.notice.NoticeRepository;
import egovframework.kt.dxp.api.domain.notice.NoticeUserRepository;
import egovframework.kt.dxp.api.domain.proposal.ProposalRepository;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageRepository;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyRepository;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyUserRepository;
import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.domain.vote.VoteRepository;
import egovframework.kt.dxp.api.domain.vote.VoteUserRepository;
import egovframework.kt.dxp.api.entity.L_SURV_USR;
import egovframework.kt.dxp.api.entity.L_VOTE_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("applicationContextHolder")
public class MyPageService {

    private final UserRepository userRepository;
    //private final VersionRepository versionRepository;
    private final VoteRepository voteRepository;
    private final VoteUserRepository voteUserRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyUserRepository surveyUserRepository;
    private final PushMessageRepository pushMessageRepository;
    private final NoticeUserRepository noticeUserRepository;
    private final ProposalRepository proposalRepository;
    private final NoticeRepository noticeRepository;
    private final MessageConfig messageConfig;
    private static final String PRPS_DIV_COMPLETED  = "02";  // 검토 완료

    /**
     * 마이페이지 조회
     *
     * @return MyPageResponse 마이페이지 조회 결과
     * @author MinJi Chae
     * @since 2024-10-21<br />
     */
    @Transactional
    public ItemResponse<MyPageResponse> getSearchMyPageList() {
        Integer resultCount;
        Integer proposalParticipationCount;
        Integer voteParticipationCount;
        Integer surveyParticipationCount;

        String userId = CommonUtils.getUserId();

        UseYn pushMessageYn = CommonUtils.checkPushMessageYn(userId, pushMessageRepository, noticeRepository);

        // 카운팅 기간 : 결과가 나왔을 경우, 카운팅에서 마이너스 됨
        // TODO: 정책 제안 건수 리턴
        // 결과 기준 proposalProgressDivisionCode가 02인 경우
        // 사용자의 정책 총 건수
        proposalParticipationCount = proposalRepository.countByCreateUserId(userId);
        // 사용자의 정책 결과 건수
        resultCount = proposalRepository.countByCreateUserIdAndProposalProgressDivisionCodeKeyCodeId(userId, PRPS_DIV_COMPLETED);
        proposalParticipationCount = proposalParticipationCount - resultCount;

        // TODO: 투표 참여 건수 리턴
        // 결과 기준 endYn이 Y인 경우
        // 사용자의 투표 참여 리스트를 전달 받아 Sequence 를 중복 제거 한다.
        List<L_VOTE_USR> voteUsrList = voteUserRepository.findByKeyUserId(userId);

        // voteSequenceNumber 기준으로 중복 제거 후 List<Integer>로 반환
        List<Integer> voteSequenceDeduplicationList = voteUsrList.stream()
                                                                 .map(voteUsr -> voteUsr.getKey().getVoteSequenceNumber())
                                                                 .distinct()
                                                                 .toList();

        voteParticipationCount = voteSequenceDeduplicationList.size();
        resultCount = voteRepository.countByVoteSequenceNumberInAndEndYn(voteSequenceDeduplicationList, UseYn.Y);
        voteParticipationCount = voteParticipationCount - resultCount;

        // TODO: 설문 참여 건수 리턴
        // 결과 기준 endYn이 Y인 경우
        List<L_SURV_USR> survUsrList = surveyUserRepository.findByKeyUserId(userId);

        List<Integer> surveySequenceDeduplicationList = survUsrList.stream()
                                                                   .map(surveyUsr -> surveyUsr.getKey().getSurveySequenceNumber())
                                                                   .distinct()
                                                                   .toList();

        surveyParticipationCount = surveySequenceDeduplicationList.size();
        resultCount = surveyRepository.countBySurveySequenceNumberInAndEndYn(surveySequenceDeduplicationList, UseYn.Y);
        surveyParticipationCount = surveyParticipationCount - resultCount;

        MyPageResponse mypageResponse = MyPageResponse.builder()
                                                      .proposalCount(proposalParticipationCount)
                                                      .voteCount(voteParticipationCount)
                                                      .surveyCount(surveyParticipationCount)
                                                      .pushMessageYn(pushMessageYn)
                                                      .build();
        return ItemResponse.<MyPageResponse>builder()
                .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                .item(mypageResponse)
                .build();
    }
}
