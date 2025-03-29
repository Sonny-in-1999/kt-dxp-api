package egovframework.kt.dxp.api.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author GEONLEE
 * @since 2024-11-07
 */
@Component
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "spring.batch.enable", havingValue = "true")
public class BatchScheduler {
    private final JobLauncher jobLauncher;

    private final Job sendMessageJob;

    @Scheduled(cron = "0 * * * * *")
    public void runPushMessageJob() throws JobExecutionException {
        this.jobLauncher.run(this.sendMessageJob, new JobParametersBuilder()
                //Batch 실행 시 Parameter 가 같으면 같은 동작으로 인식. 매번 새로운 파라메터 전달
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }

    /**
     * Application 실행 시 최호 1회 Spring Batch 실행
     */
    @Bean
    public ApplicationRunner runPushMessageJobOnStartup() {
        return args -> {
            this.jobLauncher.run(this.sendMessageJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        };
    }
}
