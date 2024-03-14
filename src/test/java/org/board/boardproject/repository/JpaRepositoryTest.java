package org.board.boardproject.repository;

import org.board.boardproject.config.JpaConfig;
import org.board.boardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
// Import 어노테이션은 테스트단에서 EnableJpaAuditing 어노테이션이 자동으로 생성되지 않아 잡아줘야한다
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    // 생성자 주입 패턴
    public JpaRepositoryTest(
                            @Autowired ArticleRepository articleRepository,
                            @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_WhenSelecting_thenWorksFine() { // given, and , then방식으로 test 진행
        // Given

        // When
        List<Article> articles =  articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert Test")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#Spring"));

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }
    @DisplayName("Update Test")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // 1L이 없으면 throw를 통해 테스트를 끝내버리자
        String updateHashingTag = "#spingboot";
        article.setHashtag(updateHashingTag);

        // When
        Article savedArticle = articleRepository.saveAndFlush(article);
        // 테스트를 구동 시 rollback을 생각하기에 update와 같은 변경사항들은 다시 돌아가야하므로 hibernate에는 select로 바뀌어 출력된다
        // save와 동시에 flush를 함으로써 hibernate가 동작하도록 지정
        // articleRepository.flush(); -영속성을 독립성으로 적용하여 hibernate가 구문을 실행하도록 한다

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updateHashingTag); // 필드안에 값과 property가 있는 지 여부 체크
    }
    @DisplayName("delete Test")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        // 삭제 전 레포지토리의 튜플의 개수를 파악
        long previousArticleCount = articleRepository.count();
        // Article oneToMany(일대다) 속성에 Cascade - 삭제를 걸어뒀기 때문에 같이 삭제되는 지 여부를 파악해야한다
        long previousArticleCommentCount = articleCommentRepository.count();
        // 지워진 comment 사이즈를 확인
        int deletedCommentsSize = article.getArticlecomments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}