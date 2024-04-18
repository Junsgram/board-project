package org.board.boardproject.dto.response;

import org.board.boardproject.dto.ArticleWithCommentsDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse (Long id,
                                         String title,
                                         String content,
                                         String hashtag,
                                         LocalDateTime createdAt,
                                         String email,
                                         String nickname,
                                          String userId,
                                         Set<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

    public static ArticleWithCommentResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentsResponse) {
        return new ArticleWithCommentResponse(id, title, content, hashtag, createdAt, email, nickname, userId,articleCommentsResponse);
    }

    public static ArticleWithCommentResponse from(ArticleWithCommentsDTO dto) {
        String nickname = dto.userAccountDTO().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDTO().userId();
        }

        return new ArticleWithCommentResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDTO().email(),
                nickname,
                dto.userAccountDTO().userId(),
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
