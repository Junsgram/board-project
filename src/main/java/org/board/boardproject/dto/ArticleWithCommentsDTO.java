package org.board.boardproject.dto;

import org.board.boardproject.domain.Article;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsDTO (
        Long id,
        UserAccountDTO userAccountDTO,
        Set<ArticleCommentDTO> articleCommentDtos,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleWithCommentsDTO of(Long id, UserAccountDTO userAccountDto, Set<ArticleCommentDTO> articleCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleWithCommentsDTO(id, userAccountDto, articleCommentDtos, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleWithCommentsDTO from(Article entity) {
        return new ArticleWithCommentsDTO(
                entity.getId(),
                UserAccountDTO.from(entity.getUserAccount()),
                entity.getArticlecomments().stream()
                        .map(ArticleCommentDTO::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}