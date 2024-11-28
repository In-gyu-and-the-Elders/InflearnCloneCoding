package inflearn_clone.springboot.utils;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapper {
    private static final Map<String, String> NAME_TO_CODE = new HashMap<>();
    private static final Map<String, String> CODE_TO_NAME = new HashMap<>();

    static {
        NAME_TO_CODE.put("개발·프로그래밍", "D");
        NAME_TO_CODE.put("인공지능", "A");
        NAME_TO_CODE.put("하드웨어", "H");
        NAME_TO_CODE.put("커리어·자기계발", "C");
        NAME_TO_CODE.put("기획·경영·마케팅", "M");

        // 역방향 매핑
        CODE_TO_NAME.put("D", "개발·프로그래밍");
        CODE_TO_NAME.put("A", "인공지능");
        CODE_TO_NAME.put("H", "하드웨어");
        CODE_TO_NAME.put("C", "커리어·자기계발");
        CODE_TO_NAME.put("M", "기획·경영·마케팅");
    }

    // 이름 -> 코드
    public static String getCode(String name) {
        return NAME_TO_CODE.getOrDefault(name, null);
    }

    // 코드 -> 이름
    public static String getName(String code) {
        return CODE_TO_NAME.getOrDefault(code, null);
    }
}
