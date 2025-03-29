package egovframework.kt.dxp.api.config.batch.survey;

import egovframework.kt.dxp.api.domain.survey.repository.SurveyRepository;
import egovframework.kt.dxp.api.domain.survey.repository.SurveyUserRepository;
import egovframework.kt.dxp.api.entity.L_SURV;
import egovframework.kt.dxp.api.entity.L_SURV_USR;
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
import java.time.LocalDateTime;
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
public class SurveyBatchConfig {

    private final SurveyRepository surveyRepository;
    private final SurveyUserRepository surveyUserRepository;

    @Bean
    public Job surveyStatisticsJob(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new JobBuilder("surveyStatisticsJob")
                .repository(jobRepository)
                .start(surveyStatisticsStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step surveyStatisticsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("surveyStatisticsStep")
                .<L_SURV, L_SURV>chunk(100)
                .reader(surveyStatisticsItemReader())
                .writer(surveyStatisticsItemWriter())
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(@Nonnull StepExecution stepExecution) {
                        log.info("Start to survey statistics Job");
                    }

                    @Override
                    public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
                        log.info("End to survey statistics Job, Status: {}", stepExecution.getStatus());
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<L_SURV> surveyStatisticsItemReader() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeMinutes = now.minusMinutes(2);
        List<L_SURV> items = surveyRepository.findByEndDateBetweenAndEndYnOrderByEndDateAsc(beforeMinutes, now, UseYn.N);
        if (items.size() == 0) {
            log.info("There is no survey data to count.");
        }
        //TODO. ListItemReader 는 this.list.remove(0) <- 성능이슈 있음. 속도 문제 있다면 LinkedList 로 변경한 class 구현 필요.
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<L_SURV> surveyStatisticsItemWriter() {
        return items -> {
            for (L_SURV survey : items) {
                //endYn 을 'Y' 로, update date 를 현재 시간으로 update
                survey.setEndYn(UseYn.Y);

                //전체 설문 인원 조회
                List<L_SURV_USR> surveyUsers = surveyUserRepository.findByKeySurveySequenceNumber(survey.getSurveySequenceNumber());
                int participantSize = surveyUsers.stream().collect(Collectors.groupingBy(user -> user.getKey().getUserId())).keySet().size();
                //TODO 실시간 정보 조회 시 참고: participantSize == 참여자 수

                //선택 질문 별로 Group by
                Map<Integer, List<L_SURV_USR>> questionGroupedMap = surveyUsers.stream()
                        .collect(Collectors.groupingBy(surveyUser -> surveyUser.getKey().getQuestionSequenceNumber()));

                Set<Integer> questionSequenceNumbers = questionGroupedMap.keySet();

                for (Integer questionSequenceNumber : questionSequenceNumbers) {
                    List<L_SURV_USR> questionParticipatedUserList = questionGroupedMap.get(questionSequenceNumber);
                    //질문에 참여한 사람
                    int questionParticipatedUserSize = questionParticipatedUserList.size();

                    //질문의 항목별로 Group by
                    Map<Integer, List<L_SURV_USR>> itemGroupedMap = questionParticipatedUserList.stream()
                            .collect(Collectors.groupingBy(surveyUser -> surveyUser.getKey().getItemSequenceNumber()));
                    Set<Integer> itemSequenceNumbers = itemGroupedMap.keySet();
                    for (Integer itemSequenceNumber : itemSequenceNumbers) {
                        //설문 결과 저장
                        //TODO 실시간 정보 조회 시 참고: selectedCount 항목 선택 수
                        Integer selectedCount = itemGroupedMap.get(itemSequenceNumber).size();
                        //TODO 실시간 정보 조회 시 참고: selectedRate 항목 참여 비율 (소수점 2자리)
                        Double selectedRate = new BigDecimal((double) selectedCount / questionParticipatedUserSize)
                                .setScale(2, RoundingMode.HALF_UP).doubleValue();
                    }
                }
            }
        };
    }
}
