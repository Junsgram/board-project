package org.board.boardproject.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@TestComponent
public class FormDataEncoder {

    private final ObjectMapper mapper;

    public FormDataEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public String encode(Object obj) {
        // TypeReference에 변환하려는 객체의 타입을 명시적으로 지정하여 사용 예정
        TypeReference<Map<String, String>> typeReference = new TypeReference<>() {};

        // ObjectMapper를 사용하여 객체를 Map으로 변환합니다.
        Map<String, String> fieldMap = mapper.convertValue(obj, typeReference);

        // Map을 MultiValueMap으로 변환
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.setAll(fieldMap);
        System.out.println(valueMap);

        // MultiValueMap을 사용하여 쿼리 파라미터를 생성
        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }

}
