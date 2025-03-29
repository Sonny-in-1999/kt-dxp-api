package egovframework.kt.dxp.api.config.batch.pushMessage;

import egovframework.kt.dxp.api.config.firebase.SendMessageExecutor;
import egovframework.kt.dxp.api.config.firebase.record.MessageRequest;
import egovframework.kt.dxp.api.config.firebase.record.NotificationResultResponse;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageMapper;
import egovframework.kt.dxp.api.domain.pushMessage.PushMessageRepository;
import egovframework.kt.dxp.api.entity.L_PUSH_MSG;
import egovframework.kt.dxp.api.entity.enumeration.TransmissionDivision;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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

/**
 * @author GEONLEE
 * @since 2024-10-29
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.batch.enable", havingValue = "true")
public class PushMessageBatchConfig {

    private final PushMessageRepository pushMessageRepository;
    private static final PushMessageMapper pushMessageMapper = PushMessageMapper.INSTANCE;

    @Bean
    public Job sendMessageJob(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new JobBuilder("sendMessageJob")
                .repository(jobRepository)
                .start(sendMessageStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step sendMessageStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendMessageStep")
                .<L_PUSH_MSG, L_PUSH_MSG>chunk(500)
                .reader(sendMessageItemReader())
                .writer(sendMessageItemWriter())
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(@Nonnull StepExecution stepExecution) {
                        log.info("Start to message transmission Job");
                    }

                    @Override
                    public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
                        log.info("End to message transmission Job, Status: {}", stepExecution.getStatus());
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<L_PUSH_MSG> sendMessageItemReader() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeMinutes = now.minusMinutes(2);
        List<L_PUSH_MSG> items = pushMessageRepository.getUndeliveredMessages(now, beforeMinutes, TransmissionDivision.N);
        if(items.size() == 0) {
            log.info("There is no data to send the message.");
        }
        //TODO. ListItemReader 는 this.list.remove(0) <- 성능이슈 있음. 속도 문제 있다면 LinkedList 로 변경한 class 구현 필요.
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<L_PUSH_MSG> sendMessageItemWriter() {
        return items -> {
            List<MessageRequest> list = pushMessageMapper.toSendMessageRequestList(items);
            SendMessageExecutor sendMessageExecutor = new SendMessageExecutor();
            List<NotificationResultResponse> resultList = sendMessageExecutor.sendMessage(list);
            //TODO. JdbcTemplate 으로 변경해서 성능 테스트 필요.
            for (int i = 0, n = resultList.size(); i < n; i++) {
                if (resultList.get(i).sendYn() == TransmissionDivision.Y) {
                    L_PUSH_MSG entity = items.get(i);
                    entity.updateTransmissionYn(TransmissionDivision.Y);
                    pushMessageRepository.save(entity);
                }
            }
        };
    }
}
