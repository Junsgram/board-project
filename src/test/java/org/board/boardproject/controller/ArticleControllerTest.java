package org.board.boardproject.controller;

import org.board.boardproject.config.SecurityConfig;
import org.board.boardproject.dto.ArticleWithCommentsDTO;
import org.board.boardproject.dto.UserAccountDTO;
import org.board.boardproject.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@DisplayName(" View 컨트롤러 - 게시글 ")
@Import(SecurityConfig.class)
// webMvcTest는 지정하지 않으면 모든 컨트롤러를 바라보기에 해당 클래스를 지정해서 사용
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    // field
    private final MockMvc mvc;

    // 입 출력만 확인하기 위해 연결을 끊기 위해 @MockBean 어노테이션을 사용한다
    // Mockito mock과 유사한 어노테이션
    // parameter값에 사용할 수 없어 필드값으로 지정
    @MockBean private ArticleService articleService;

    // constructor
    public ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mvc = mockMvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null),any(Pageable.class)))
                .willReturn(Page.empty());
        // any메소드는 Argumentsmathcers로 필드값을 모두 동일하게 matchers로 맞춰야해서 eq(동등) 값을 준다 - eq는 null값이어야 하므로 eq로 값으 매쳐
        // When & Then
        mvc.perform(get("/articles"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/index"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("articles"));
        then(articleService).should().searchArticles(eq(null), eq(null),any(Pageable.class));

    }


    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        // When & Then
        mvc.perform(get("/articles/1"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/detail"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("article_comment"));
        then(articleService).should().getArticle(articleId);

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

    // createArticleWithCommentsDto
    private ArticleWithCommentsDTO createArticleWithCommentsDto() {
        return ArticleWithCommentsDTO.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "Jun",
                LocalDateTime.now(),
                "jun"
        );
    }
    private UserAccountDTO createUserAccountDto() {
        return UserAccountDTO.of(
                1L,
                "Jun",
                "pw",
                "jun@mail.com",
                "Jun",
                "memo",
                LocalDateTime.now(),
                "Jun",
                LocalDateTime.now(),
                "jun"
        );
    }


}