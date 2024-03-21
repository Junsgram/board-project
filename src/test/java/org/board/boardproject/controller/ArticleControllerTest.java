package org.board.boardproject.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@DisplayName(" View 컨트롤러 - 게시글 ")
// webMvcTest는 지정하지 않으면 모든 컨트롤러를 바라보기에 해당 클래스를 지정해서 사용
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    // field
    private final MockMvc mvc;
    // constructor
    public ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mvc = mockMvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        mvc.perform(get("/articles"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/index"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("articles"));
    }


    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        mvc.perform(get("/articles/1"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("aritcle"))
                .andExpect(model().attributeExists("aritcleComments"));
    }
    @Disabled("구현 중 ")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        mvc.perform(get("/article/search"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }

    @Disabled("구현 중 ")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        mvc.perform(get("/article/search-hastag"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/hastag"));
    }
}