package com.strayanimal.schedulerservice.api.util;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimalColorExtractor {

    // 긴 표현부터 먼저 검사해야 오인식이 줄어듭니다.
    private static final LinkedHashMap<String, List<String>> PHRASE_COLOR_MAP = new LinkedHashMap<>();

    // 짧은 축약형 문자 단위 토큰
    private static final Map<String, String> SHORT_TOKEN_MAP = new HashMap<>();

    // 색상 없는 패턴/설명 키워드
    private static final List<String> NON_COLOR_PATTERNS = Arrays.asList(
            "줄무늬", "얼룩무늬", "호반무늬", "호랑이무늬", "호피무늬",
            "고등어", "고등어태비", "고등어테비", "치즈태비", "태비",
            "턱시도", "삼색묘", "삼색이", "삼색", "무늬", "반점",
            "호구", "브린들", "믹스", "카오스", "거북이",
            "털끝검정", "평행전체부분줄무늬"
    );

    static {
        // ===== 복합/예외 표현 =====
        PHRASE_COLOR_MAP.put("검은색흰색황토색조합", list("검정색", "흰색", "노란색", "갈색"));
        PHRASE_COLOR_MAP.put("검정색황색", list("검정색", "노란색"));
        PHRASE_COLOR_MAP.put("황색검정색", list("노란색", "검정색"));
        PHRASE_COLOR_MAP.put("검정황색", list("검정색", "노란색"));
        PHRASE_COLOR_MAP.put("황색흰색", list("노란색", "흰색"));
        PHRASE_COLOR_MAP.put("흰색황색", list("흰색", "노란색"));

        PHRASE_COLOR_MAP.put("엷은황갈색", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("황갈색", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("황토색", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("적갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("적갈", list("갈색"));
        PHRASE_COLOR_MAP.put("붉은연갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("연갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("연한갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("엹은갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("옅은황색", list("노란색"));
        PHRASE_COLOR_MAP.put("엷은황갈색흰색", list("노란색", "갈색", "흰색"));
        PHRASE_COLOR_MAP.put("붉고엷은황갈색", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("금갈색", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("흑갈색", list("검정색", "갈색"));
        PHRASE_COLOR_MAP.put("검갈색", list("검정색", "갈색"));
        PHRASE_COLOR_MAP.put("회갈색", list("회색", "갈색"));
        PHRASE_COLOR_MAP.put("잿빛갈색", list("회색", "갈색"));
        PHRASE_COLOR_MAP.put("갈레드색", list("갈색"));
        PHRASE_COLOR_MAP.put("쵸콜릿색", list("갈색"));
        PHRASE_COLOR_MAP.put("초콜릿색", list("갈색"));
        PHRASE_COLOR_MAP.put("짙은브라운", list("갈색"));
        PHRASE_COLOR_MAP.put("브라운", list("갈색"));
        PHRASE_COLOR_MAP.put("밤갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("밤색", list("갈색"));

        PHRASE_COLOR_MAP.put("크림색", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("크림", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("아이보리", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("베이지색", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("베이지", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("레몬색", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("치즈색", list("노란색"));
        PHRASE_COLOR_MAP.put("치즈", list("노란색"));

        PHRASE_COLOR_MAP.put("노랑치즈", list("노란색"));
        PHRASE_COLOR_MAP.put("노랑흰색", list("노란색", "흰색"));
        PHRASE_COLOR_MAP.put("노랑흰", list("노란색", "흰색"));
        PHRASE_COLOR_MAP.put("연황색", list("노란색"));
        PHRASE_COLOR_MAP.put("황백색", list("노란색", "흰색"));
        PHRASE_COLOR_MAP.put("황백", list("노란색", "흰색"));
        PHRASE_COLOR_MAP.put("백황", list("흰색", "노란색"));
        PHRASE_COLOR_MAP.put("검백", list("검정색", "흰색"));
        PHRASE_COLOR_MAP.put("흰검", list("흰색", "검정색"));
        PHRASE_COLOR_MAP.put("백회", list("흰색", "회색"));
        PHRASE_COLOR_MAP.put("회백", list("회색", "흰색"));
        PHRASE_COLOR_MAP.put("백갈", list("흰색", "갈색"));
        PHRASE_COLOR_MAP.put("갈백", list("갈색", "흰색"));
        PHRASE_COLOR_MAP.put("백흑", list("흰색", "검정색"));
        PHRASE_COLOR_MAP.put("흑백", list("검정색", "흰색"));
        PHRASE_COLOR_MAP.put("흑황", list("검정색", "노란색"));
        PHRASE_COLOR_MAP.put("흑갈", list("검정색", "갈색"));
        PHRASE_COLOR_MAP.put("갈흑", list("갈색", "검정색"));
        PHRASE_COLOR_MAP.put("갈흰", list("갈색", "흰색"));
        PHRASE_COLOR_MAP.put("검갈", list("검정색", "갈색"));
        PHRASE_COLOR_MAP.put("검흰", list("검정색", "흰색"));
        PHRASE_COLOR_MAP.put("흰갈", list("흰색", "갈색"));

        PHRASE_COLOR_MAP.put("흑갈백색", list("검정색", "갈색", "흰색"));
        PHRASE_COLOR_MAP.put("흑갈백", list("검정색", "갈색", "흰색"));
        PHRASE_COLOR_MAP.put("백갈흑색", list("흰색", "갈색", "검정색"));
        PHRASE_COLOR_MAP.put("백갈흑", list("흰색", "갈색", "검정색"));
        PHRASE_COLOR_MAP.put("갈흑백", list("갈색", "검정색", "흰색"));
        PHRASE_COLOR_MAP.put("갈검흰", list("갈색", "검정색", "흰색"));
        PHRASE_COLOR_MAP.put("갈흰검", list("갈색", "흰색", "검정색"));
        PHRASE_COLOR_MAP.put("흰갈검", list("흰색", "갈색", "검정색"));
        PHRASE_COLOR_MAP.put("회검흰", list("회색", "검정색", "흰색"));
        PHRASE_COLOR_MAP.put("흑회백색", list("검정색", "회색", "흰색"));
        PHRASE_COLOR_MAP.put("흰색검갈색", list("흰색", "검정색", "갈색"));
        PHRASE_COLOR_MAP.put("검갈흰", list("검정색", "갈색", "흰색"));
        PHRASE_COLOR_MAP.put("검갈백", list("검정색", "갈색", "흰색"));
        PHRASE_COLOR_MAP.put("갈검", list("갈색", "검정색"));

        PHRASE_COLOR_MAP.put("검회색", list("검정색", "회색"));
        PHRASE_COLOR_MAP.put("검회", list("검정색", "회색"));
        PHRASE_COLOR_MAP.put("갈검색", list("갈색", "검정색")); // 오타 방어
        PHRASE_COLOR_MAP.put("검겅과흰색", list("검정색", "흰색")); // 오타 방어

        PHRASE_COLOR_MAP.put("은색", list("회색"));
        PHRASE_COLOR_MAP.put("청회색", list("회색"));
        PHRASE_COLOR_MAP.put("어두운회색", list("회색"));
        PHRASE_COLOR_MAP.put("울프그레이", list("회색"));
        PHRASE_COLOR_MAP.put("그레이지", list("회색", "갈색"));
        PHRASE_COLOR_MAP.put("청색", list("회색"));
        PHRASE_COLOR_MAP.put("회청색", list("회색"));

        PHRASE_COLOR_MAP.put("금색", list("노란색"));
        PHRASE_COLOR_MAP.put("황색", list("노란색"));
        PHRASE_COLOR_MAP.put("노랑색", list("노란색"));
        PHRASE_COLOR_MAP.put("노란색", list("노란색"));
        PHRASE_COLOR_MAP.put("노랑", list("노란색"));
        PHRASE_COLOR_MAP.put("겨자색", list("노란색"));
        PHRASE_COLOR_MAP.put("살구색", list("노란색"));
        PHRASE_COLOR_MAP.put("탄", list("노란색", "갈색"));
        PHRASE_COLOR_MAP.put("블랙탄", list("검정색", "노란색", "갈색"));

        PHRASE_COLOR_MAP.put("주황색", list("주황색"));
        PHRASE_COLOR_MAP.put("주황", list("주황색"));

        PHRASE_COLOR_MAP.put("하양", list("흰색"));
        PHRASE_COLOR_MAP.put("흰색", list("흰색"));
        PHRASE_COLOR_MAP.put("백", list("흰색"));

        PHRASE_COLOR_MAP.put("검정색", list("검정색"));
        PHRASE_COLOR_MAP.put("검은색", list("검정색"));
        PHRASE_COLOR_MAP.put("검정", list("검정색"));
        PHRASE_COLOR_MAP.put("흑색", list("검정색"));
        PHRASE_COLOR_MAP.put("흑", list("검정색"));

        PHRASE_COLOR_MAP.put("갈색", list("갈색"));
        PHRASE_COLOR_MAP.put("갈", list("갈색"));

        PHRASE_COLOR_MAP.put("회색", list("회색"));
        PHRASE_COLOR_MAP.put("회", list("회색"));

        // ===== 짧은 축약형 =====
        SHORT_TOKEN_MAP.put("백", "흰색");
        SHORT_TOKEN_MAP.put("흰", "흰색");
        SHORT_TOKEN_MAP.put("검", "검정색");
        SHORT_TOKEN_MAP.put("흑", "검정색");
        SHORT_TOKEN_MAP.put("갈", "갈색");
        SHORT_TOKEN_MAP.put("황", "노란색");
        SHORT_TOKEN_MAP.put("노", "노란색");
        SHORT_TOKEN_MAP.put("회", "회색");
        SHORT_TOKEN_MAP.put("주", "주황색");
    }

    private static final LinkedHashMap<String, String> PATTERN_MAP = new LinkedHashMap<>();

    static {
        PATTERN_MAP.put("평행전체부분줄무늬", "줄무늬");
        PATTERN_MAP.put("줄무늬", "줄무늬");
        PATTERN_MAP.put("얼룩무늬", "얼룩무늬");
        PATTERN_MAP.put("호반무늬", "호반무늬");
        PATTERN_MAP.put("호랑이무늬", "호랑이무늬");
        PATTERN_MAP.put("호피무늬", "호피무늬");
        PATTERN_MAP.put("고등어태비", "고등어태비");
        PATTERN_MAP.put("고등어테비", "고등어태비");
        PATTERN_MAP.put("고등어", "고등어태비");
        PATTERN_MAP.put("치즈태비", "치즈태비");
        PATTERN_MAP.put("태비", "고등어태비");
        PATTERN_MAP.put("테비", "고등어태비");
        PATTERN_MAP.put("턱시도", "턱시도");
        PATTERN_MAP.put("삼색묘", "삼색");
        PATTERN_MAP.put("삼색이", "삼색");
        PATTERN_MAP.put("삼색", "삼색");
        PATTERN_MAP.put("브린들", "브린들");
        PATTERN_MAP.put("호구", "호구");
        PATTERN_MAP.put("카오스", "카오스");
        PATTERN_MAP.put("믹스", "믹스");
        PATTERN_MAP.put("거북이", "거북이");
    }

    public static List<String> extractColors(String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }

        String text = normalizeText(raw);

        // 패턴만 있는 경우에도 빈값 대신 패턴 반환
        if (isPatternOnly(text)) {
            List<String> res = extractPatternValues(text);
            res.add(0, "pattern");

            return res;
        }

        LinkedHashSet<String> result = new LinkedHashSet<>();

        // 1) 긴 표현부터 우선 추출
        for (Map.Entry<String, List<String>> entry : PHRASE_COLOR_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                result.addAll(entry.getValue());
            }
        }

        // 2) 남은 축약 토큰 처리
        extractShortTokens(text, result);

        return new ArrayList<>(result);
    }

    public static Map<String, List<String>> extractColorsFromMultiline(String multilineText) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (multilineText == null || multilineText.isBlank()) {
            return result;
        }

        String[] lines = multilineText.split("\\R");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                result.put(trimmed, extractColors(trimmed));
            }
        }
        return result;
    }

    private static String normalizeText(String raw) {
        String text = raw.trim();

        // 괄호/구분자 통일
        text = text.replace("기타(", "")
                .replace("(", "")
                .replace(")", "")
                .replace("/", " ")
                .replace("&", " ")
                .replace("+", " ")
                .replace(",", " ")
                .replace(".", " ")
                .replace("·", "")
                .replace("  ", " ");

        // 자주 나오는 불필요 설명 제거
        text = text.replace("조합", " ")
                .replace("말단", " ")
                .replace("바탕에", " ")
                .replace("바탕", " ")
                .replace("한가지색", " ")
                .replace("두가지색", " ")
                .replace("명확한", " ")
                .replace("또는", " ")
                .replace("얼굴과", " ")
                .replace("다리쪽은", " ")
                .replace("등쪽은", " ")
                .replace("어둡고", " ")
                .replace("옅은색", " ")
                .replace("털", " ")
                .replace("반점", " 반점 ")
                .replace("무늬", " 무늬 ");

        // 오타 보정
        text = text.replace("검겅", "검정")
                .replace("엹은", "옅은")
                .replace("노랑색", "노란색");

        return text.replaceAll("\\s+", " ").trim();
    }

    private static boolean isPatternOnly(String text) {
        boolean hasColorHint =
                text.contains("흰") || text.contains("백") ||
                        text.contains("검") || text.contains("흑") ||
                        text.contains("갈") ||
                        text.contains("노랑") || text.contains("노란") || text.contains("황") || text.contains("금") ||
                        text.contains("회") || text.contains("은") || text.contains("청회") || text.contains("그레이") ||
                        text.contains("주황") ||
                        text.contains("크림") || text.contains("아이보리") || text.contains("베이지") || text.contains("레몬");

        if (hasColorHint) {
            return false;
        }

        for (String pattern : NON_COLOR_PATTERNS) {
            if (text.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static void extractShortTokens(String text, LinkedHashSet<String> result) {
        Matcher matcher = Pattern.compile("[가-힣]+").matcher(text);
        while (matcher.find()) {
            String token = matcher.group();

            // 이미 긴 표현으로 잡힌 경우가 많으므로, 아주 짧은 축약형 중심으로만 사용
            if (token.length() <= 3) {
                for (int i = 0; i < token.length(); i++) {
                    String ch = String.valueOf(token.charAt(i));
                    String mapped = SHORT_TOKEN_MAP.get(ch);
                    if (mapped != null) {
                        result.add(mapped);
                    }
                }
            }
        }
    }

    private static List<String> list(String... values) {
        return Arrays.asList(values);
    }

    private static List<String> extractPatternValues(String text) {
        LinkedHashSet<String> result = new LinkedHashSet<>();

        text = normalizeText(text);

        // 1. 사전 정의된 패턴 매칭
        for (Map.Entry<String, String> entry : PATTERN_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                result.add(entry.getValue());
            }
        }

        // 2. 사전에 없는 패턴인데 "무늬", "태비" 같은 단어가 있으면 원문 일부 반환
        if (result.isEmpty()) {
            Matcher matcher = Pattern.compile("([가-힣]+(?:무늬|태비|줄무늬|얼룩무늬))").matcher(text);
            while (matcher.find()) {
                result.add(matcher.group(1));
            }
        }

        // 3. 그래도 없으면 원문 자체 반환
        if (result.isEmpty()) {
            result.add(text);
        }

        return new ArrayList<>(result);
    }

}
