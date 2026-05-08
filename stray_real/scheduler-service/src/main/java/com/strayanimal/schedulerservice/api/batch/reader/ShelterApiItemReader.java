package com.strayanimal.schedulerservice.api.batch.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@StepScope
public class ShelterApiItemReader implements ItemReader<PetShelter> {

    private final Iterator<PetShelter> dataIterator;

    public ShelterApiItemReader() {
        List<PetShelter> results = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        // API에서 보호 상태 필터 (protect: 보호중, notice: 공고중)
        List<String> states = List.of("protect", "notice");

        try {
            for (String state : states) {
                int pageNo = 1;
                int numOfRows = 500;
                int totalCount;
                String serviceKey = "8f9eaa77d16f15956feee347df08423d25a4027ba135615541d84a1910a0ea8e";

                do {
                    // API 호출용 URL 조립
                    String url = String.format(
                            "http://apis.data.go.kr/1543061/animalShelterSrvc_v2/shelterInfo_v2?serviceKey=%s&_type=json&numOfRows=%d&pageNo=%d",
                            serviceKey, numOfRows, pageNo);

                    // Http 요청을 생성: 지정된 URL로 GET 방식 호출을 준비
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url)) // API 주소 (현재 페이지에 해당)
                            .GET()                // GET 요청 방식 사용
                            .build();

                    // HttpClient를 사용해 API 호출 실행 및 응답 수신 (String 형태로 받음)
                    HttpResponse<String> response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString()); // 문자열로 응답 받음

                    // 받은 응답 문자열을 JSON 객체로 파싱
                    // 구조: response -> body -> items -> item (배열 또는 단일 객체)
                    JsonNode body = mapper.readTree(response.body())  // JSON 전체 트리로 변환
                            .path("response")                         // 첫 번째 루트
                            .path("body");                            // 실제 데이터가 들어있는 body

                    // 응답에서 동물 데이터 항목들(item)을 가져옴
                    JsonNode items = body.path("items").path("item");

                    // 총 데이터 개수를 가져옴 → 페이징 종료 조건 계산에 사용됨
                    totalCount = body.path("totalCount").asInt(); // 전체 데이터 개수

                    // item이 배열인 경우 (복수 개체): 하나씩 엔티티로 변환해서 리스트에 저장
                    if (items.isArray()) {
                        for (JsonNode item : items) {
                            results.add(parseToEntity(item)); // JSON → AnimalsEntity 변환 후 저장
                        }
                        // item이 단일 객체인 경우: 바로 변환해서 리스트에 저장
                    } else if (items.isObject()) {
                        results.add(parseToEntity(items)); // JSON → AnimalsEntity
                    }

                    // 다음 페이지로 이동하기 위해 pageNo 증가
                    // ex) pageNo=1 → 2 → 3 ...
                    pageNo++;

                    // 현재까지 가져온 데이터 수 = (pageNo - 1) * numOfRows
                    // 이 수치가 totalCount 보다 작으면 다음 페이지가 존재하므로 계속 반복
                    // 예: 총 1200건, 한 페이지 500건이면 3페이지까지 반복됨
                } while ((pageNo - 1) * numOfRows < totalCount);

            }
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패", e);
        }

        // 수집한 데이터를 Iterator로 설정하여 read() 호출 시 순차 제공
        this.dataIterator = results.iterator();
    }


    @Override
    public PetShelter read() {
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    private PetShelter parseToEntity(JsonNode item) {
        return PetShelter.builder()
                .careRegNo(item.path("careRegNo").asText())
                .dataStdDt(item.path("dataStdDt").asText(null))          // 데이터기준일자
                .careNm(item.path("careNm").asText(null))                // 동물보호센터명
                .orgNm(item.path("orgNm").asText(null))                  // 관리기관명
                .divisionNm(item.path("divisionNm").asText(null))        // 보호센터유형
                .saveTrgtAnimal(item.path("saveTrgtAnimal").asText(null))// 구조대상동물
                .careAddr(item.path("careAddr").asText(null))            // 도로명주소
                .jibunAddr(item.path("jibunAddr").asText(null))          // 지번주소
                .lat(item.path("lat").asText(null))                      // 위도
                .lng(item.path("lng").asText(null))                      // 경도
                .dsignationDate(item.path("dsignationDate").asText(null))// 지정일자
                .weekOprStime(item.path("weekOprStime").asText(null))    // 평일운영시작
                .weekOprEtime(item.path("weekOprEtime").asText(null))    // 평일운영종료
                .weekCellStime(item.path("weekCellStime").asText(null))  // 평일분양시작
                .weekCellEtime(item.path("weekCellEtime").asText(null))  // 평일분양종료
                .weekendOprStime(item.path("weekendOprStime").asText(null)) // 주말운영시작
                .weekendOprEtime(item.path("weekendOprEtime").asText(null)) // 주말운영종료
                .weekendCellStime(item.path("weekendCellStime").asText(null)) // 주말분양시작
                .weekendCellEtime(item.path("weekendCellEtime").asText(null)) // 주말분양종료
                .closeDay(item.path("closeDay").asText(null))            // 휴무일
                .vetPersonCnt(item.path("vetPersonCnt").asText(null))    // 수의사수
                .specsPersonCnt(item.path("specsPersonCnt").asText(null))// 사양관리사수
                .medicalCnt(item.path("medicalCnt").asText(null))        // 진료실수
                .breedCnt(item.path("breedCnt").asText(null))            // 사육실수
                .quarabtineCnt(item.path("quarabtineCnt").asText(null))  // 격리실수
                .feedCnt(item.path("feedCnt").asText(null))              // 사료보관실수
                .transCarCnt(item.path("transCarCnt").asText(null))      // 구조차량수
                .careTel(item.path("careTel").asText(null))              // 전화번호
                .build();
    }

}
