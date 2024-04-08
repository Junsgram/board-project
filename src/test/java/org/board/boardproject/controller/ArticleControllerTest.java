package org.board.boardproject.controller;

import org.board.boardproject.config.TestSecurityConfig;
import org.board.boardproject.domain.constant.FormStatus;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.request.ArticleRequest;
import org.board.boardproject.dto.response.ArticleResponse;
import org.board.boardproject.service.ArticleService;
import org.board.boardproject.service.PaginationService;
import org.board.boardproject.util.FormDataEncoder;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@DisplayName(" View 컨트롤러 - 게시글 ")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
// webMvcTest는 지정하지 않으면 모든 컨트롤러를 바라보기에 해당 클래스를 지정해서 사용
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    // field
    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    // 입 출력만 확인하기 위해 연결을 끊기 위해 @MockBean 어노테이션을 사용한다
    // Mockito mock과 유사한 어노테이션
    // parameter값에 사용할 수 없어 필드값으로 지정
    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    // constructor
    public ArticleControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null),any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        // any메소드는 Argumentsmathcers로 필드값을 모두 동일하게 matchers로 맞춰야해서 eq(동등) 값을 준다 - eq는 null값이어야 하므로 eq로 값으 매쳐
        // When & Then
        mvc.perform(get("/articles"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/index"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());

    }
    @DisplayName("[view][GET] 게시글 상세 페이지 - 인증이 정상적으로 통과하지 않았을 때 로그인 페이지로 이동")
    @Test
    public void givenNothing_whenRequestingArticle_thenRedirectToLoginPage() throws Exception {
        // Given
        long articleId = 1L;
        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(articleService).shouldHaveNoInteractions();
        then(articleService).shouldHaveNoInteractions();
    }

    @WithMockUser // 인증 정보를 통과를 진행시키는 메소드
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출 확인, 인증된 사용자 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        long totalCount = 1L;
        given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());

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
        then(articleService).should().getArticleWithComments(articleId);
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출 확인 ")
    @Test
    // test mehtod
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given
        List<String> hashtags = List.of("#java","#Spring","#boot");
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(articleService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출 확인, 해시태그 입력한 경우 테스트 ")
    @Test
    // test mehtod
    public void givenHashtag_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java","#Spring","#boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        given(articleService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        // When & Then
        mvc.perform(
                get("/articles/search-hashtag")
                        .queryParam("searchKeyword", hashtag)

        )
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags",hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어를 제공하고 해당되는 내용을 출력 ")
    @Test
    // test mehtod
    public void givenSomethingKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";

        given(articleService.searchArticles(eq(searchType), eq(searchKeyword),any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        // any메소드는 Argumentsmathcers로 필드값을 모두 동일하게 matchers로 맞춰야해서 eq(동등) 값을 준다 - eq는 null값이어야 하므로 eq로 값으 매쳐
        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("searchType", searchType.name())
                                .queryParam("searchKeyword", searchKeyword)
                )
                // 200 ok가 진행되었는 지 확인하는 메소드 stauts()
                .andExpect(status().isOk())
                // content의 내용의 타입을 확인 - view라서 text_html로 진행
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/index"))
                // view단에서 model의 키의 값들이 있는 지 확인
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType), eq(searchKeyword),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewArticlePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    /*
     *  WithUserDetails 구현된 userDetails를 사용할 수 있는 어노테이션 단, 유저정보도 DB에 있어야한다
     *  setupBefore = TestExecutionEvent.TEST_EXECUTION : 테스트를 진행하기 전에 지정된 테스트를 마치고 테스트를 진행
     *  userDetailsServiceBeanName = "userDetailService" -> securityConfig에서 구현된 userDetails를 기입
     *  value = junTest의 값은 TestSecurity에 지정된 값을 기입
     */
    @WithUserDetails(value = "junTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequesting_thenSavesNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).saveArticle(any(ArticleDTO.class));

        // When & Then
        mvc.perform(
                        post("/articles/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().saveArticle(any(ArticleDTO.class));
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지 - 인증 완료")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdatedArticlePage() throws Exception {
        // Given
        long articleId = 1L;
        ArticleDTO dto = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(articleService).should().getArticle(articleId);
    }


    @DisplayName("[view][POST] 게시글 수정 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    public void givenNothing_whenRequesting_thenRedirectToLoginPage() throws Exception {
        // Given
        long articleId = 1L;
        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(articleService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "junTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출 , 인증 완료")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).updateArticle(eq(articleId), any(ArticleDTO.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleService).should().updateArticle(eq(articleId), any(ArticleDTO.class));
    }

    @WithUserDetails(value = "junTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출, 인증 완료")
    @Test
    void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long articleId = 1L;
        String userId = "junTest";
        willDoNothing().given(articleService).deleteArticle(articleId, userId);

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().deleteArticle(articleId, userId);
    }


    private ArticleDTO createArticleDto() {
        return ArticleDTO.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleWithCommentsDTO createArticleWithCommentsDto() {
        return ArticleWithCommentsDTO.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "jun",
                LocalDateTime.now(),
                "jun"
        );
    }

    private UserAccountDTO createUserAccountDto() {
        return UserAccountDTO.of(
                "jun",
                "pw",
                "jun@mail.com",
                "Jun",
                "memo",
                LocalDateTime.now(),
                "jun",
                LocalDateTime.now(),
                "jun"
        );
    }

}