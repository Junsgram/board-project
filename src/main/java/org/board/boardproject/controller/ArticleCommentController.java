package org.board.boardproject.controller;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.dto.UserAccountDTO;
import org.board.boardproject.dto.request.ArticleCommentRequest;
import org.board.boardproject.dto.request.ArticleRequest;
import org.board.boardproject.service.ArticleCommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDTO.of(
                "Jun", "pw", "Jun@mail.com", null, null
        )));
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable("commentId") Long commentId, Long articleId) {
        articleCommentService.deleteArticleComment(commentId);
        return "redirect:/articles/" + articleId;
    }


}
