package org.board.boardproject.dto.request;

import org.board.boardproject.dto.ArticleCommentDTO;
import org.board.boardproject.dto.UserAccountDTO;

public record ArticleCommentRequest(
        Long articleId,
        String content
)
{
    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDTO toDto(UserAccountDTO userAccountDTO) {
        return ArticleCommentDTO.of(
                articleId,
                userAccountDTO,
                content
        );
    }
}
