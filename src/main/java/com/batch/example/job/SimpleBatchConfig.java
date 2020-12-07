package com.batch.example.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용합니다.
public class SimpleBatchConfig {
    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    /*
    처음 jdbc로 DB 연결 시 spring_batch.batch_job_instance 테이블이 없어서 실행이 되지 않는다.
    batch-core에서 schema-mysql.sql을 찾아서 해당 테이블들을 생성해준다.
    schema-mysql.sql를 통해 생성되는게 메타 테이블들이다.
     */

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requstDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();

        /*
        tasklet : Step 안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용한다.
         */
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requstDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
        /*
        tasklet : Step 안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용한다.
         */
    }
}
