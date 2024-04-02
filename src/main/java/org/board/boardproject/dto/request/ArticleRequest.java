package org.board.boardproject.dto.request;

import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.UserAccountDTO;

public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {

    public static ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDTO toDto(UserAccountDTO userAccountDTO) {
        return ArticleDTO.of(
                userAccountDTO,
                title,
                content,
                hashtag
        );
    }

}