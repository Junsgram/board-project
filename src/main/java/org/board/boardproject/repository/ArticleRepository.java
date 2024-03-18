package org.board.boardproject.repository;

import org.board.boardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// spring Data REST 사용하는 어노테이션 yml파일에 annotation으로 지정하였기 때문에 어노테이션으로 지정해야한다
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
