package org.board.boardproject.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.QArticle;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.ArticleWithCommentsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

// spring Data REST 사용하는 어노테이션 yml파일에 annotation으로 지정하였기 때문에 어노테이션으로 지정해야한다
@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        // Article안에 있는 모든 필드에 대한 기본 검색기능을 추가
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle>{

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // QuerydslPredicateExecutor로 인하여 모든 검색기능이 추가되었지만, 메소드를 활용해서 제외 및 추가를 진행할 예정
        // 리스팅을 하지 않는 프로퍼티는 검색에서 제외하는 메소드
        bindings.excludeUnlistedProperties(true);
        // root 기반으로 검색기능을 추가하는 메소드
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        // first 메소드에는 람다식으로 (path, value) -> {}가 기본으로 진행되고 ** Expression으로 변경하여 사용
        // containsIgnoreCase는 대소문자를 구분하지 않고 검색하고 like와 차이점은 오른쪽 주석 확인
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%{value}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%{value}%'
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        // bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '{value}'
    }
}
