package org.board.boardproject.dto;

import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.ArticleComment;

import java.time.LocalDateTime;

public record ArticleCommentDTO (
    Long id,
    Long articleId,
    UserAccountDTO userAccountDTO,
    String content,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy
) {

    public static ArticleCommentDTO of(Long id, Long articleId, UserAccountDTO userAccountDto, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
            return new ArticleCommentDTO(id, articleId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy);
        }

        public static ArticleCommentDTO from(ArticleComment entity) {
            return new ArticleCommentDTO(
                    entity.getId(),
                    entity.getArticle().getId(),
                    UserAccountDTO.from(entity.getUserAccount()),
                    entity.getContent(),
                    entity.getCreatedAt(),
                    entity.getCreatedBy(),
                    entity.getModifiedAt(),
                    entity.getModifiedBy()
            );
        }

        public ArticleComment toEntity(Article entity) {
            return ArticleComment.of(
                    entity,
                    userAccountDTO.toEntity(),
                    content
            );
        }
    }
