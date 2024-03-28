package org.board.boardproject.service;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.ArticleWithCommentsDTO;
import org.board.boardproject.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDTO getArticle(Long articleId) {
        return null;
    }

    public void saveArticle(ArticleDTO dto) {
    }

    public void updateArticle(ArticleDTO dto ) {
    }

    public void deleteArticle(Long articleId) {
    }
}
