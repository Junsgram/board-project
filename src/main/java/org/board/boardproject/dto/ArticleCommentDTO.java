package org.board.boardproject.dto;

import java.time.LocalDateTime;

public class ArticleCommentDTO {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String content;

    private ArticleCommentDTO(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, String content) {
    }

    public static ArticleCommentDTO of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
                                       String modifiedBy, String content) {
        return new ArticleCommentDTO(createdAt, createdBy, modifiedAt, modifiedBy, content);
    }
}
