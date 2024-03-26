package org.board.boardproject.service;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.dto.ArticleCommentDTO;
import org.board.boardproject.repository.ArticleCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDTO> searchArticleComment(Long articleId) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDTO articleCommentDTO) {
    }
}
