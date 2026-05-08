package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.entity.CatBreed;
import com.strayanimal.schedulerservice.api.entity.DogBreed;
import com.strayanimal.schedulerservice.api.util.CatBreedFileSetMapper;
import com.strayanimal.schedulerservice.api.util.DogBreedFileSetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class CatBreedBatchConfig {

    // 배치 관련 메타데이터 저장소 (실행 정보)
    private final JobRepository jobRepository;
    // 배치에서 사용하는 트랜잭션 관리자
    private final PlatformTransactionManager transactionManager;
    // 데이터베이스 연결 정보
    private final DataSource dataSource;

    // 데이터 읽기 (ItemReader)
    @Bean
    public FlatFileItemReader<CatBreed> CatBreedReader() {
        return new FlatFileItemReaderBuilder<CatBreed>()
                .name("petCultureCsvReader") // 이름 지어주기
                // 어떤 파일을 읽을것인가
                // 현재 사용할 디렉토리의 위치는 본인에 맞게 수정해야함.
                .resource(new FileSystemResource("C:\\Users\\user\\Desktop\\python_prac\\데이터셋\\cat_csv\\cat_breeds.csv"))
                .delimited() // 쉼표로 구분된 csv 파일입니다.
                .names(
                        "name",
                        "length",
                        "origin",
                        "min_life_expectancy",
                        "max_life_expectancy",
                        "min_weight",
                        "max_weight",
                        "family_friendly",
                        "shedding",
                        "general_health",
                        "playfulness",
                        "children_friendly",
                        "grooming",
                        "intelligence",
                        "other_pets_friendly"
                )
                .encoding("UTF-8")
                .fieldSetMapper(new CatBreedFileSetMapper())
                .linesToSkip(1) // 첫 줄 (헤더) 건너뛰기
                .build();
    }

    // 데이터 저장하기 (ItemWriter)
    @Bean
    public JdbcBatchItemWriter<CatBreed> CatBreedWriter() {
        return new JdbcBatchItemWriterBuilder<CatBreed>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("""
                    INSERT INTO cat_breeds (
                                 name, length, origin,
                                 min_life_expectancy, max_life_expectancy,
                                 min_weight, max_weight,
                                 family_friendly, shedding, general_health,
                                 playfulness, children_friendly,
                                 grooming, intelligence, other_pets_friendly
                             ) VALUES (
                                 :name, :length, :origin,
                                 :minLifeExpectancy, :maxLifeExpectancy,
                                 :minWeight, :maxWeight,
                                 :familyFriendly, :shedding, :generalHealth,
                                 :playfulness, :childrenFriendly,
                                 :grooming, :intelligence, :otherPetsFriendly
                             )
                             ON DUPLICATE KEY UPDATE
                                 length = VALUES(length),
                                 origin = VALUES(origin),
                                 min_life_expectancy = VALUES(min_life_expectancy),
                                 max_life_expectancy = VALUES(max_life_expectancy),
                                 min_weight = VALUES(min_weight),
                                 max_weight = VALUES(max_weight),
                                 family_friendly = VALUES(family_friendly),
                                 shedding = VALUES(shedding),
                                 general_health = VALUES(general_health),
                                 playfulness = VALUES(playfulness),
                                 children_friendly = VALUES(children_friendly),
                                 grooming = VALUES(grooming),
                                 intelligence = VALUES(intelligence),
                                 other_pets_friendly = VALUES(other_pets_friendly)
                    """)
                .dataSource(dataSource)
                .build();

    }

    // 작업단계 만들기 (step)
    @Bean
    public Step csvToDbStepCat() {
        return new StepBuilder("csvToDbStepCat", jobRepository)
                // <AnimalHospital, AnimalHospital>: Reader에서 읽어온 타입과 Writer로 전달하는 데이터 타입 명시
                // chunk: step이 작업을 처리할 때 기준에 맞춰 나눠서 작업을 처리.
                // chunk(500): 500개씩 묵어서 처리, 단위별로 작업 후 commit, 문제가 있다면 rollback
                // 단위를 나눠놓지 않으면 전체 데이터가 rollback 되기 때문에, 작은 단위로 나눠 작업을 진행
                .<CatBreed, CatBreed>chunk(500, transactionManager)
                .reader(CatBreedReader())
                .writer(CatBreedWriter())
                .build();
    }

    // 전체 작업 정의하기 (Job)
    @Bean
    public Job csvToDbJobCat() {
        return new JobBuilder("csvToDbJobCat", jobRepository)
                .start(csvToDbStepCat())
                .build();
    }

}
