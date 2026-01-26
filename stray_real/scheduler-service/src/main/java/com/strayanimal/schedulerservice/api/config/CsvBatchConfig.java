package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.entity.AnimalHospital;
import com.strayanimal.schedulerservice.api.util.AnimalHospitalFieldSetMapper;
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

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CsvBatchConfig {

    // 배치 관련 메타데이터 저장소 (실행 정보)
    private final JobRepository jobRepository;
    // 배치에서 사용하는 트랜잭션 관리자
    private final PlatformTransactionManager transactionManager;
    // 데이터베이스 연결 정보
    private final DataSource dataSource;

    // 데이터 읽기 (ItemReader)
    @Bean
    public FlatFileItemReader<AnimalHospital> animalHospitalReader() {
        return new FlatFileItemReaderBuilder<AnimalHospital>()
                .name("animalHospitalCsvReader") // 이름 지어주기
                // 어떤 파일을 읽을것인가
                // 현재 사용할 디렉토리의 위치는 본인에 맞게 수정해야함.
                .resource(new FileSystemResource("C:\\nyangmong_image\\hospital\\animal_hospital.csv"))
                .delimited() // 쉼표로 구분된 csv 파일입니다.
                .names(
                        "id",
                        "regionCode",
                        "managementNumber",
                        "approvalDate",
                        "permitCancelDate",
                        "businessStatusName",
                        "closureDate",
                        "temporaryClosureStartDate",
                        "temporaryClosureEndDate",
                        "reopeningDate",
                        "siteArea",
                        "postalCode",
                        "roadPostalCode",
                        "businessName",
                        "dataUpdateType",
                        "rightsHolderNumber",
                        "dataUpdateDate",
                        "roadAddress",
                        "detailedBusinessStatusName",
                        "detailedBusinessStatusCode",
                        "phoneNumber",
                        "coordinateX",
                        "coordinateY",
                        "fullAddress",
                        "lastModified"
                )
                .encoding("UTF-8")
                .fieldSetMapper(new AnimalHospitalFieldSetMapper())
                .linesToSkip(1) // 첫 줄 (헤더) 건너뛰기
                .build();
    }

    // 데이터 저장하기 (ItemWriter)
    @Bean
    public JdbcBatchItemWriter<AnimalHospital> animalHospitalWriter() {
        return new JdbcBatchItemWriterBuilder<AnimalHospital>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("""
                        INSERT INTO animal_hospital (
                            id, region_code, management_number, approval_date, permit_cancel_date,
                            business_status_name, closure_date, temporary_closure_start_date,
                            temporary_closure_end_date, reopening_date, site_area, postal_code, road_postal_code,
                            business_name, data_update_type, rights_holder_number, data_update_date, road_address,
                            detailed_business_status_name, detailed_business_status_code, phone_number,
                            coordinate_x, coordinate_y, full_address, last_modified
                        ) VALUES (
                            :id, :regionCode, :managementNumber, :approvalDate, :permitCancelDate,
                            :businessStatusName, :closureDate, :temporaryClosureStartDate,
                            :temporaryClosureEndDate, :reopeningDate, :siteArea, :postalCode, :roadPostalCode,
                            :businessName, :dataUpdateType, :rightsHolderNumber, :dataUpdateDate, :roadAddress,
                            :detailedBusinessStatusName, :detailedBusinessStatusCode, :phoneNumber,
                            :coordinateX, :coordinateY, :fullAddress, :lastModified
                        )
                        
                        ON DUPLICATE KEY UPDATE
                        
                            region_code = VALUES(region_code),
                            management_number = VALUES(management_number),
                            approval_date = VALUES(approval_date),
                            permit_cancel_date = VALUES(permit_cancel_date),
                        
                            business_status_name = VALUES(business_status_name),
                            closure_date = VALUES(closure_date),
                        
                            temporary_closure_start_date = VALUES(temporary_closure_start_date),
                            temporary_closure_end_date = VALUES(temporary_closure_end_date),
                            reopening_date = VALUES(reopening_date),
                        
                            site_area = VALUES(site_area),
                            postal_code = VALUES(postal_code),
                            road_postal_code = VALUES(road_postal_code),
                        
                            business_name = VALUES(business_name),
                        
                            data_update_type = VALUES(data_update_type),
                            data_update_date = VALUES(data_update_date),
                        
                            rightsHolderNumber = VALUES(rightsHolderNumber),
                        
                            roadAddress = VALUES(roadAddress),
                        
                            detailedBusinessStatusName = VALUES(detailedBusinessStatusName),
                            detailedBusinessStatusCode = VALUES(detailedBusinessStatusCode),
                        
                            phoneNumber = VALUES(phoneNumber),
                        
                            coordinate_x = VALUES(coordinate_x),
                            coordinate_y = VALUES(coordinate_y),
                        
                            fullAddress = VALUES(fullAddress),
                        
                            lastModified = VALUES(lastModified);
                        
                        """)
                .dataSource(dataSource) // 데이터베이스 정보 전달
                .build();

    }

    // 작업단계 만들기 (step)
    @Bean
    public Step csvToDbStep() {
        return new StepBuilder("csvToDbStep", jobRepository)
                // <AnimalHospital, AnimalHospital>: Reader에서 읽어온 타입과 Writer로 전달하는 데이터 타입 명시
                // chunk: step이 작업을 처리할 때 기준에 맞춰 나눠서 작업을 처리.
                // chunk(500): 500개씩 묵어서 처리, 단위별로 작업 후 commit, 문제가 있다면 rollback
                // 단위를 나눠놓지 않으면 전체 데이터가 rollback 되기 때문에, 작은 단위로 나눠 작업을 진행
                .<AnimalHospital, AnimalHospital>chunk(500, transactionManager)
                .reader(animalHospitalReader())
                .writer(animalHospitalWriter())
                .build();
    }

    // 전체 작업 정의하기 (Job)
    @Bean
    public Job csvToDbJob() {
        return new JobBuilder("csvToDbJob", jobRepository)
                .start(csvToDbStep())
                .build();
    }

}