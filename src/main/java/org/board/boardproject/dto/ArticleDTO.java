package org.board.boardproject.dto;

import org.board.boardproject.domain.Article;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleDTO(
        Long id,
        UserAccountDTO userAccountDTO,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleDTO of(Long id, UserAccountDTO userAccountDto, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDTO(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDTO from(Article entity) {
        return new ArticleDTO(
                entity.getId(),
                UserAccountDTO.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Article toEntity() {
        return Article.of(
                userAccountDTO.toEntity(),
                title,
                content,
                hashtag
        );
    }

}
