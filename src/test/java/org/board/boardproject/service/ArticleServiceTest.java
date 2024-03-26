package org.board.boardproject.service;

import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.ArticleUpdateDTO;
import org.board.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

/**
 * 스프링 부트 스라이스 테스트가 아닌 목킹방식으로 mokito방식으로 테스트 진행
 * 검색
 * 각 게시글 페이지 이동
 * 페이지 네이션
 * 홈 버튼 -> 게시판 페이지로 리다이렉션
 * 정렬 기능
 */
@DisplayName("비즈니스 로직 - 게시글 테스트 구현")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    // system under test : 시스템 테스트 대상
    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList()  {
        // Given
        // When
        Page<ArticleDTO> articles = sut.searchArticles(SearchType.TITLE,"search keyword" );
        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면 게시글을 반환한다.")
    @Test
    void givenId_whenSearchingArticle_thenReturnsArticle()  {
        // Given
        // When
       ArticleDTO articles = sut.searchArticle(1L);
        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면 게시글 생성")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        // Given
        ArticleDTO dto = ArticleDTO.of("Title","Content", "title", LocalDateTime.now(),"Jun");
            // save method가 호출될 것이라는 것을 명시해놓은 코드
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // When
        sut.saveArticle(dto);
        // Then
            // save method가 한 번 호출이 되었는 지 확인하는 메소드
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID와 수정정보를 입력하면 게시글 수정")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        ArticleUpdateDTO dto = ArticleUpdateDTO.of("Update Title","Update Content", "Update Hashtag");
        // save method가 호출될 것이라는 것을 명시해놓은 코드
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // When
        sut.updateArticle(1L , dto);
        // Then
        // save method가 한 번 호출이 되었는 지 확인하는 메소드
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID 전달하면 게시글 삭제")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        ArticleUpdateDTO dto = ArticleUpdateDTO.of("Update Title","Update Content", "Update Hashtag");
        // save method가 호출될 것이라는 것을 명시해놓은 코드
        willDoNothing().given(articleRepository).delete(any(Article.class));
        // When
        sut.deleteArticle(1L);
        // Then
        // save method가 한 번 호출이 되었는 지 확인하는 메소드
        then(articleRepository).should().delete(any(Article.class));
    }
}