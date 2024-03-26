package org.board.boardproject.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleDTO(LocalDateTime createdAt,
                         String title,
                         String content,
                         String hashtag,
                         String createdBy
){

    public static ArticleDTO of(String title, String content, String hashtag, LocalDateTime createdAt, String createdBy) {
        return new ArticleDTO(createdAt,createdBy,content,title,hashtag);
    }
}
