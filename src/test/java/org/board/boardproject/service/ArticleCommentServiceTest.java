package org.board.boardproject.service;

import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.ArticleComment;
import org.board.boardproject.domain.UserAccount;
import org.board.boardproject.dto.ArticleCommentDTO;
import org.board.boardproject.repository.ArticleCommentRepository;
import org.board.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService sut;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Disabled
    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnArticleComments() {
        // Given
        Long articleId = 1L;
        UserAccount user = UserAccount.of("Jun", "1234","Jun@mail","Jun", "테스트입니다.");

        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of(user,"title","content","#Java")
        ));
        // When
        List<ArticleCommentDTO> articleComments = sut.searchArticleComment(1L);
        // Then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 정보를 입력하면 , 댓글을 입력한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(ArticleCommentDTO.of(LocalDateTime.now(), "Jun", LocalDateTime.now(), "Jun", "comment"));

        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

}