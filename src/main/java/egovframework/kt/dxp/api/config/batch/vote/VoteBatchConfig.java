package egovframework.kt.dxp.api.config.batch.vote;

import egovframework.kt.dxp.api.domain.user.UserRepository;
import egovframework.kt.dxp.api.domain.vote.VoteRepository;
import egovframework.kt.dxp.api.domain.vote.VoteUserRepository;
import egovframework.kt.dxp.api.entity.L_VOTE;
import egovframework.kt.dxp.api.entity.L_VOTE_USR;
import egovframework.kt.dxp.api.entity.enumeration.UseYn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author GEONLEE
 * @since 2024-10-30
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.batch.enable", havingValue = "true")
public class VoteBatchConfig {

    private final VoteRepository voteRepository;
    private final VoteUserRepository voteUserRepository;
    private final UserRepository userRepository;

    @Bean
    public Job voteStatisticsJob(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new JobBuilder("voteStatisticsJob")
                .repository(jobRepository)
                .start(voteStatisticsStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step voteStatisticsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("voteStatisticsStep")
                .<L_VOTE, L_VOTE>chunk(100)
                .reader(voteStatisticsItemReader())
                .writer(voteStatisticsItemWriter())
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(@Nonnull StepExecution stepExecution) {
                        log.info("Start to vote statistics Job");
                    }

                    @Override
                    public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
                        log.info("End to vote statistics Job, Status: {}", stepExecution.getStatus());
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<L_VOTE> voteStatisticsItemReader() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeMinutes = now.minusMinutes(2);
        List<L_VOTE> items = voteRepository.findByEndDateBetweenAndEndYnOrderByEndDateAsc(beforeMinutes, now, UseYn.N);
        if (items.size() == 0) {
            log.info("There is no voted data to count.");
        }
        //TODO. ListItemReader 는 this.list.remove(0) <- 성능이슈 있음. 속도 문제 있다면 LinkedList 로 변경한 class 구현 필요.
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<L_VOTE> voteStatisticsItemWriter() {
        return items -> {
            for (L_VOTE vote : items) {
                //endYn 을 'Y' 로, update date 를 현재 시간으로 update
                vote.setEndYn(UseYn.Y);
                vote.setUpdateDate(LocalDateTime.now());

                //기준 대상자(성별, 나이), 참여자 수를 구한 후 L_VOTE 에 추가
                String gender = null;
                int targetCount;
                LocalDate[] dateRange = getDateRangeFromAge(vote.getParticipationStartAge(), vote.getParticipationEndAge());
                if (UseYn.N == vote.getMaleAbleYn() && UseYn.Y == vote.getFemaleAbleYn()) {
                    gender = "F";
                } else if (UseYn.N == vote.getFemaleAbleYn() && UseYn.Y == vote.getMaleAbleYn()) {
                    gender = "M";
                }
                if (gender == null) {
                    targetCount = userRepository.countByBirthDateBetween(dateRange[0].toString(), dateRange[1].toString());
                } else {
                    targetCount = userRepository.countByBirthDateBetweenAndGenderType(dateRange[0].toString(), dateRange[1].toString(), gender);
                }
                //TODO 실시간 정보 조회 시 참고: targetCount == 대상자 수

                //전체 투표 인원 조회
                List<L_VOTE_USR> voteUsers = voteUserRepository.findByKeyVoteSequenceNumber(vote.getVoteSequenceNumber());
                int participantSize = voteUsers.size();
                //TODO 실시간 정보 조회 시 참고: participantSize == 참여자 수

                //선택항목 별로 Group by
                Map<Integer, List<L_VOTE_USR>> groupedMap = voteUsers.stream()
                        .collect(Collectors.groupingBy(voteUser -> voteUser.getKey().getItemSequenceNumber()));

                Set<Integer> itemSequenceNumbers = groupedMap.keySet();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                for (Integer itemSequenceNumber : itemSequenceNumbers) {
                    //투표 결과 통계 테이블 데이터 생성
                    //TODO 실시간 정보 조회 시 참고: selectedCount 항목 선택 수
                    int selectedCount = groupedMap.get(itemSequenceNumber).size();
                    //TODO 실시간 정보 조회 시 참고: selectedRate 항목 참여 비율 (소수점 2자리)
                    Double selectedRate = new BigDecimal((double) selectedCount / participantSize)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();

                    //투표 결과 연령 통계 테이블 데이터 생성
                    List<L_VOTE_USR> itemSelectedVoteUser = groupedMap.get(itemSequenceNumber);
                    Map<Integer, Long> groupedByAgeRange = itemSelectedVoteUser.stream()
                            .collect(Collectors.groupingBy(voteUser -> {
                                int age = Period.between(
                                        LocalDate.parse(voteUser.getUser().getBirthDate(), formatter),
                                        LocalDate.now()).getYears();
                                int ageRange = (age / 10) * 10;
                                return Math.min(ageRange, 80); //10, 20, 30... 리턴하기 위함, 80보다 크면 80으로
                            }, Collectors.counting()));
                    //연령별 집계를 위한 Map 생성
                    Map<Integer, Integer> voteCountByAgeRange = new HashMap<>(Map.of(
                            10, 0, 20, 0, 30, 0, 40, 0,
                            50, 0, 60, 0, 70, 0, 80, 0));
                    // 각 연령대별 수를 집계
                    //TODO 실시간 정보 조회 시 참고: TODO 연령별 참여 비율
//                    voteCountByAgeRange.get(20),
//                    voteCountByAgeRange.get(30),
//                    voteCountByAgeRange.get(40),
//                    voteCountByAgeRange.get(50),
//                    voteCountByAgeRange.get(60),
//                    voteCountByAgeRange.get(70),
//                    voteCountByAgeRange.get(80)
                }
            }
        };
    }

    private LocalDate[] getDateRangeFromAge(int startAge, int endAge) {
        LocalDate today = LocalDate.now();

        LocalDate endDate = today.minus(endAge, ChronoUnit.YEARS).plusDays(1);
        LocalDate startDate = today.minus(startAge, ChronoUnit.YEARS);

        return new LocalDate[]{endDate, startDate};
    }
}
