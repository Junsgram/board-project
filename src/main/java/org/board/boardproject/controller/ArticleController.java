package org.board.boardproject.controller;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.response.ArticleResponse;
import org.board.boardproject.dto.response.ArticleWithCommentResponse;
import org.board.boardproject.service.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public String articles(
            // required = false 없으면 전체 게시글 조회 있으면 값을 찾는다
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchKeyword,
            @PageableDefault(size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        map.addAttribute("articles", articleService.searchArticles(searchType,searchKeyword,pageable)
                .map(ArticleResponse::from));
        return "/articles/index";
    }
    @GetMapping("/{articleId}")
    public String articles(@PathVariable("articleId") Long articleId, ModelMap map) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        return "/articles/detail";
    }
}
