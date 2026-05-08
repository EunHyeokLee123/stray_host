package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.batch.processor.ShelterProcessor;
import com.strayanimal.schedulerservice.api.batch.reader.ShelterApiItemReader;
import com.strayanimal.schedulerservice.api.batch.writer.ShelterCustomItemWriter;
import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ShelterSyncJobConfig {

    // Spring Batch의 내부 실행 상태 저장소
    private final JobRepository jobRepository;

    // 트랜잭션 처리를 위한 매니저 (보통 JPA 트랜잭션 매니저)
    private final PlatformTransactionManager transactionManager;

    // 동물 API에서 데이터를 읽는 ItemReader
    private final ShelterApiItemReader reader;

    // 읽어온 데이터를 처리 (현재는 그대로 반환하는 Processor)
    private final ShelterProcessor processor;

    // DB에 데이터를 저장/업데이트하는 ItemWriter
    private final ShelterCustomItemWriter writer;

    // Step 종료 후 불필요한 데이터를 삭제하는 Listener
    private final ShelterStepListener listener;

    /**
     * Step 정의 - 'animalApiToDbStep'
     * - 기능: API에서 데이터를 읽고, 가공하고, DB에 저장
     * - 처리 단위(Chunk size): 300개씩 트랜잭션으로 묶어 처리
     */
    @Bean
    public Step shelterApiToDbStep() {
        return new StepBuilder("shelterApiToDbStep", jobRepository)
                // <Input 타입, Output 타입> 설정
                .<PetShelter, PetShelter>chunk(300, transactionManager)
                // Reader: API에서 읽기
                .reader(reader)
                // Processor: 가공 (현재는 그대로 반환)
                .processor(processor)
                // Writer: DB에 저장 또는 업데이트
                .writer(writer)
                .listener(listener)
                .build();
    }

    /**
     * Job 정의 - 'syncAnimalJob'
     * - 하나의 Step(apiToDbStep)을 순차적으로 실행하는 단일 Step Job 구성
     * - Job 실행 시 자동으로 Step이 시작됨
     */
    @Bean
    public Job syncShelterJob() {
        return new JobBuilder("syncShelterJob", jobRepository)
                .start(shelterApiToDbStep()) // 시작 Step 지정
                .build();
    }

}
