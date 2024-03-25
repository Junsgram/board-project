package org.board.boardproject.controller;

import org.board.boardproject.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 인증 ")
@Import(SecurityConfig.class)
@WebMvcTest
public class AuthControllerTest {
    // field
    private final MockMvc mvc;
    // constructor
    public AuthControllerTest(@Autowired MockMvc mockMvc) {
        this.mvc = mockMvc;
    }

    @DisplayName("[view][GET] 로그인 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenTryingLogin_thenReturnLoginView() throws Exception {
        // Given

        // When & Then
        // login 페이지를 작성하지 않았지만 SpringSecurity를 통해 login테스트가 정상 작동하는 것을 볼 수 있다.
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
