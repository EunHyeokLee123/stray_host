package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.entity.DogBreed;
import com.strayanimal.schedulerservice.api.entity.PetCulture;
import com.strayanimal.schedulerservice.api.util.DogBreedFileSetMapper;
import com.strayanimal.schedulerservice.api.util.PetCultureFielSetMapper;
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
public class DogBreedBatchConfig {

    // 배치 관련 메타데이터 저장소 (실행 정보)
    private final JobRepository jobRepository;
    // 배치에서 사용하는 트랜잭션 관리자
    private final PlatformTransactionManager transactionManager;
    // 데이터베이스 연결 정보
    private final DataSource dataSource;


    // 데이터 읽기 (ItemReader)
    @Bean
    public FlatFileItemReader<DogBreed> DogBreedReader() {
        return new FlatFileItemReaderBuilder<DogBreed>()
                .name("petCultureCsvReader") // 이름 지어주기
                // 어떤 파일을 읽을것인가
                // 현재 사용할 디렉토리의 위치는 본인에 맞게 수정해야함.
                .resource(new FileSystemResource("C:\\Users\\user\\Desktop\\python_prac\\데이터셋\\dog_csv\\archive\\dog_breeds.csv"))
                .delimited() // 쉼표로 구분된 csv 파일입니다.
                .names(
                        "key",
                        "name",
                        "image_link",
                        "energy",
                        "trainability",
                        "protectiveness",
                        "shedding",
                        "barking",
                        "playfulness",
                        "grooming",
                        "drooling",
                        "coat_length",
                        "good_with_other_dogs",
                        "good_with_strangers",
                        "min_life_expectancy",
                        "max_life_expectancy",
                        "min_height_male",
                        "max_height_male",
                        "min_height_female",
                        "max_height_female",
                        "min_weight_male",
                        "max_weight_male",
                        "min_weight_female",
                        "max_weight_female",
                        "life_expectancy",
                        "origin",
                        "description",
                        "temperament",
                        "colors",
                        "size_category",
                        "popularity_score",
                        "breed_group",
                        "breed_function",
                        "coat_type",
                        "children_friendly",
                        "apartment_friendly",
                        "novice_owner_friendly",
                        "prey_drive",
                        "exercise_needs",
                        "heat_tolerance",
                        "cold_tolerance",
                        "review_status",
                        "source_registry",
                        "source_url",
                        "reviewed_at"
                )
                .encoding("UTF-8")
                .fieldSetMapper(new DogBreedFileSetMapper())
                .linesToSkip(1) // 첫 줄 (헤더) 건너뛰기
                .build();
    }

    // 데이터 저장하기 (ItemWriter)
    @Bean
    public JdbcBatchItemWriter<DogBreed> DogBreedWriter() {
        return new JdbcBatchItemWriterBuilder<DogBreed>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("""
                        INSERT INTO dog_breeds (
                                            breed_key, name, image_link,
                                            energy, trainability, protectiveness, shedding, barking,
                                            playfulness, grooming, drooling, coat_length,
                                            good_with_other_dogs, good_with_strangers,
                                            min_life_expectancy, max_life_expectancy,
                                            min_height_male, max_height_male,
                                            min_height_female, max_height_female,
                                            min_weight_male, max_weight_male,
                                            min_weight_female, max_weight_female,
                                            life_expectancy, origin, description,
                                            temperament, colors, size_category,
                                            popularity_score, breed_group, breed_function, coat_type,
                                            children_friendly, apartment_friendly, novice_owner_friendly,
                                            prey_drive, exercise_needs, heat_tolerance, cold_tolerance,
                                            review_status, source_registry, source_url, reviewed_at
                                        ) VALUES (
                                            :key, :name, :imageLink,
                                            :energy, :trainability, :protectiveness, :shedding, :barking,
                                            :playfulness, :grooming, :drooling, :coatLength,
                                            :goodWithOtherDogs, :goodWithStrangers,
                                            :minLifeExpectancy, :maxLifeExpectancy,
                                            :minHeightMale, :maxHeightMale,
                                            :minHeightFemale, :maxHeightFemale,
                                            :minWeightMale, :maxWeightMale,
                                            :minWeightFemale, :maxWeightFemale,
                                            :lifeExpectancy, :origin, :description,
                                            :temperament, :colors, :sizeCategory,
                                            :popularityScore, :breedGroup, :breedFunction, :coatType,
                                            :childrenFriendly, :apartmentFriendly, :noviceOwnerFriendly,
                                            :preyDrive, :exerciseNeeds, :heatTolerance, :coldTolerance,
                                            :reviewStatus, :sourceRegistry, :sourceUrl, :reviewedAt
                                        )
                                        ON DUPLICATE KEY UPDATE
                                            name = VALUES(name),
                                            image_link = VALUES(image_link),
                                            energy = VALUES(energy),
                                            trainability = VALUES(trainability),
                                            protectiveness = VALUES(protectiveness),
                                            shedding = VALUES(shedding),
                                            barking = VALUES(barking),
                                            playfulness = VALUES(playfulness),
                                            grooming = VALUES(grooming),
                                            drooling = VALUES(drooling),
                                            coat_length = VALUES(coat_length),
                                            good_with_other_dogs = VALUES(good_with_other_dogs),
                                            good_with_strangers = VALUES(good_with_strangers),
                                            min_life_expectancy = VALUES(min_life_expectancy),
                                            max_life_expectancy = VALUES(max_life_expectancy),
                                            min_height_male = VALUES(min_height_male),
                                            max_height_male = VALUES(max_height_male),
                                            min_height_female = VALUES(min_height_female),
                                            max_height_female = VALUES(max_height_female),
                                            min_weight_male = VALUES(min_weight_male),
                                            max_weight_male = VALUES(max_weight_male),
                                            min_weight_female = VALUES(min_weight_female),
                                            max_weight_female = VALUES(max_weight_female),
                                            life_expectancy = VALUES(life_expectancy),
                                            origin = VALUES(origin),
                                            description = VALUES(description),
                                            temperament = VALUES(temperament),
                                            colors = VALUES(colors),
                                            size_category = VALUES(size_category),
                                            popularity_score = VALUES(popularity_score),
                                            breed_group = VALUES(breed_group),
                                            breed_function = VALUES(breed_function),
                                            coat_type = VALUES(coat_type),
                                            children_friendly = VALUES(children_friendly),
                                            apartment_friendly = VALUES(apartment_friendly),
                                            novice_owner_friendly = VALUES(novice_owner_friendly),
                                            prey_drive = VALUES(prey_drive),
                                            exercise_needs = VALUES(exercise_needs),
                                            heat_tolerance = VALUES(heat_tolerance),
                                            cold_tolerance = VALUES(cold_tolerance),
                                            review_status = VALUES(review_status),
                                            source_registry = VALUES(source_registry),
                                            source_url = VALUES(source_url),
                                            reviewed_at = VALUES(reviewed_at)
                    """)
                .dataSource(dataSource)
                .build();

    }

    // 작업단계 만들기 (step)
    @Bean
    public Step csvToDbStepDog() {
        return new StepBuilder("csvToDbStepDog", jobRepository)
                // <AnimalHospital, AnimalHospital>: Reader에서 읽어온 타입과 Writer로 전달하는 데이터 타입 명시
                // chunk: step이 작업을 처리할 때 기준에 맞춰 나눠서 작업을 처리.
                // chunk(500): 500개씩 묵어서 처리, 단위별로 작업 후 commit, 문제가 있다면 rollback
                // 단위를 나눠놓지 않으면 전체 데이터가 rollback 되기 때문에, 작은 단위로 나눠 작업을 진행
                .<DogBreed, DogBreed>chunk(500, transactionManager)
                .reader(DogBreedReader())
                .writer(DogBreedWriter())
                .build();
    }

    // 전체 작업 정의하기 (Job)
    @Bean
    public Job csvToDbJobDog() {
        return new JobBuilder("csvToDbJobDog", jobRepository)
                .start(csvToDbStepDog())
                .build();
    }

}
