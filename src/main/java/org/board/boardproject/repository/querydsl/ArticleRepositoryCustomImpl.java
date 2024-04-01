package org.board.boardproject.repository.querydsl;

import com.querydsl.jpa.JPQLQuery;
import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.QArticle;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    // querydsl 구현시 사용방법
    // 생성자 생성
    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        // QEntity의 값을 꺼내옴
        QArticle article = QArticle.article;

        return from(article)
                .distinct()
                // hashtag의 컬럼만 설정
                .select(article.hashtag)
                .where(article.hashtag.isNotNull())
                .fetch();
        // fetch는 쿼리를 반환해주는 메소드이다. 위의 방법처럼 return을 전체적으로 진행할 수 있다.
        // return query.fetch();
    }
}
