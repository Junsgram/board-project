package org.board.boardproject.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleUpdateDTO(
                               String title,
                               String content,
                               String hashtag
) {

    public static ArticleUpdateDTO of(String title, String content, String hashtag) {
        return new ArticleUpdateDTO(content,title,hashtag);
    }
}
