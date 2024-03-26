package org.board.boardproject.service;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.ArticleUpdateDTO;
import org.board.boardproject.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticles(SearchType searchType, String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDTO searchArticle(long l) {
        return null;
    }

    public void saveArticle(ArticleDTO dto) {
    }

    public void updateArticle(Long articleId, ArticleUpdateDTO dto) {
    }

    public void deleteArticle(Long articleId) {
    }
}
