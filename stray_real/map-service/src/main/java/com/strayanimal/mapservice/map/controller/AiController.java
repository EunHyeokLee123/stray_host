package com.strayanimal.mapservice.map.controller;/*
package com.playdata.mapservice.map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map/ai")
@RequiredArgsConstructor
public class AiController {

    private final ChatClient chatClient;

    @GetMapping("/ai")
    String generation(@RequestParam(name = "input") String userInput) {
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("""
You are an expert SQL engineer.

You are given:
- A user's natural language question
- A table schema

Your task is to convert the user's request into an accurate SQL query.

Only return the SQL query without explanation.

Here is the table schema:

Table name: `pet_culture`

Columns:
- id: Long (Primary Key)
- facility_name: String (facility name)
- category_one: String (main category -> only one value)
- category_two: String  (sub category -> possible values includes: '반려동물 서비스', '반려동물식당카페',
                        반려동반여행, 반려문화시설, 반려의료)
- category_three: String (detailed subcategory of the place. Possible values include:\s
                                                   '동물병원', '동물약국', '문예회관', '미술관', '미용', '박물관', '반려동물용품', '식당',
                                                   '여행지', '위탁관리', '카페', '호텔', '펜션')
- sido: String (province)
- sigungu: String (district)
- legal_dong: String (town name)
- li_name: String (village name)
- lnbr_no: String (address number)
- road_name: String
- building_no: String
- mapx: Double (longitude)
- mapy: Double (latitude)
- zip_no: String (zipcode)
- road_address: String
- full_address: String
- tel_num: String (telephone number)
- url: String (homepage URL)
- rest_info: String (holiday info)
- oper_time: String (operating hours)
- parking: String (parking availability)
- price: String (price info)
- pet_with: String (pet allowed or not)
- pet_info: String
- pet_size: String
- pet_restrict: String
- in_place: String (pets allowed inside)
- out_place: String (pets allowed outside)
- info_desc: String (description)
- extra_fee: String (pet extra fee)
- last_update: Date

The user question may be in Korean. Translate it to English first if needed, then generate the SQL query based on the translated input.

Here are example questions and expected SQL outputs:

Q: 반려동물 동반 가능한 장소 알려줘.
SQL:
SELECT * FROM place WHERE pet_with = 'Y';

Q: 서울시에 있는 반려동물 동반 가능한 장소 알려줘.
SQL:
SELECT * FROM place WHERE sido = '서울특별시' AND pet_with = 'Y';

Q: 강남구에 있는 카페 중 주차 가능한 곳 알려줘.
SQL:
SELECT * FROM place WHERE sigungu = '강남구' AND category_two LIKE '%카페%' AND parking = 'Y';

Q: 경기 지역에서 반려동물과 실내 동반 가능한 식당 알려줘.
SQL:
SELECT * FROM place WHERE sido LIKE '경기%' AND category_two LIKE '%식당%' AND in_place = 'Y';

Q: 서울시에 있으면서 도로명 주소에 '중앙로'가 들어가고, 반려동물 제한사항이 없는 장소 알려줘.
SQL:
SELECT * FROM place WHERE sido = '서울특별시' AND road_address LIKE '%중앙로%' AND (pet_restrict = '해당없음' OR pet_restrict = '제한사항 없음');

Now, convert the following natural language question into a valid SQL query:
"""),
                new UserMessage(userInput)
        ));

        return this.chatClient.prompt(prompt)
                .user(userInput)
                .call()
                .content();
    }

}
*/
