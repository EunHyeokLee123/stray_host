package com.strayanimal.schedulerservice.api.controller;

import com.strayanimal.schedulerservice.api.batch.dto.ShelterDetail;
import com.strayanimal.schedulerservice.api.dto.CategoryDto;
import com.strayanimal.schedulerservice.api.dto.PetListResDto;
import com.strayanimal.schedulerservice.api.service.ColorService;
import com.strayanimal.schedulerservice.api.service.SchedulerService;
import com.strayanimal.schedulerservice.api.service.SearchTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 동물 배치 Job 실행용 REST API Controller.
 *
 * POST /api/animals/sync-api 호출 시 수동으로 배치 Job 실행.
 * 배치 중복 실행 방지를 위해 현재시간 기반 JobParameters 전달.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class SchedulerController {

    private final JobLauncher jobLauncher;
    private final Job syncAnimalJob;
    private final Job syncMapJob;
    private final Job syncShelterJob;

    private final Job csvToDbJob;

    // made By 이은혁
    private final Job csvToDbJobCulture;
    private final SchedulerService schedulerService;
    private final ColorService colorService;
    private final SearchTestService searchTestService;
    private final Job csvToDbJobDog;
    private final Job csvToDbJobCat;


    @GetMapping("/scheduler/api/animal")
    public String animalRunApiSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(syncAnimalJob, jobParameters);
            return "배치 실행 완료 - 상태: " + execution.getStatus();

        } catch (Exception e) {
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @GetMapping("/scheduler/api/shelter")
    public String shelterRunApiSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(syncShelterJob, jobParameters);
            return "배치 실행 완료 - 상태: " + execution.getStatus();

        } catch (Exception e) {
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @GetMapping("/scheduler/api/map")
    public String mapRunApiSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(syncMapJob, jobParameters);
            return "배치 실행 완료 - 상태: " + execution.getStatus();

        } catch (Exception e) {
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/csv-to-db")
    public String runCsvToDbJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJob, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/pet")
    public String runPetCultureJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJobCulture, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/cat")
    public String runCatBreedJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJobCat, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/dog")
    public String runDogBreedJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJobDog, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/detail")
    public String runPetCultureJobDetail() {

        schedulerService.petCultureToDetailTable();
        return "성공적으로 테이블로 매핑됨!";

    }

    @GetMapping("/scheduler/test")
    public ResponseEntity<?> getOnePet(@RequestParam String desNo) {
        ShelterDetail petDetail = schedulerService.getPetDetail(desNo);

        if(petDetail == null) {
            return new ResponseEntity<>("없음", HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(petDetail, HttpStatus.FOUND);
        }
    }


    // 색깔 적용
    @GetMapping("/scheduler/color")
    public ResponseEntity<?> getOnePetColor() {
        Map<String, List<String>> result = colorService.getColorMap();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 특정 조건 검색 테스트용
    @PostMapping("/scheduler/search/{page}")
    public ResponseEntity<?> getOnePetSearch(@RequestBody CategoryDto categoryDto,
                                             @PathVariable int page) {
        Page<PetListResDto> resDto =
                searchTestService.getPetSearch(categoryDto, page);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}