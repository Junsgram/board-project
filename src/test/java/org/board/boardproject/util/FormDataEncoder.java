package org.board.boardproject.util;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

// TODO : 제네릭 타입 문제 점 확인 후 보안하자
//@TestComponent
//public class FormDataEncoder {
//
//    private final ObjectMapper mapper;
//
//    public FormDataEncoder(ObjectMapper mapper) {
//        this.mapper = mapper;
//    }
//
//
//    public String encode(Object obj) {
//        Map<String, String> fieldMap = mapper.convertValue(obj, new TypeReference<>() {});
//        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//        valueMap.setAll(fieldMap);
//
//        return UriComponentsBuilder.newInstance()
//                .queryParams(valueMap)
//                .encode()
//                .build()
//                .getQuery();
//    }
//
//}
