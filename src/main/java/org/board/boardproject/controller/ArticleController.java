package org.board.boardproject.controller;

import lombok.RequiredArgsConstructor;
import org.board.boardproject.domain.constant.FormStatus;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.UserAccountDTO;
import org.board.boardproject.dto.request.ArticleRequest;
import org.board.boardproject.dto.response.ArticleResponse;
import org.board.boardproject.dto.response.ArticleWithCommentResponse;
import org.board.boardproject.dto.security.BoardPrincipal;
import org.board.boardproject.service.ArticleService;
import org.board.boardproject.service.PaginationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;


    @GetMapping
    public String articles(
            // required = false 없으면 전체 게시글 조회 있으면 값을 찾는다
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchKeyword,
            @PageableDefault(size=10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        Page<ArticleResponse> articles =  articleService.searchArticles(searchType,searchKeyword,pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(),articles.getTotalPages());
        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        return "/articles/index";

    }
    @GetMapping("/{articleId}")
    public String articles(@PathVariable("articleId") Long articleId, ModelMap map) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticleWithComments(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "/articles/detail";
    }

    // 해시태그 검색
    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(@RequestParam(required = false) String searchKeyword,
                                @PageableDefault(size=10, sort="createdAt", direction=Sort.Direction.DESC) Pageable pageable,
                                ModelMap map)
    {
        Page<ArticleResponse> articles =  articleService.searchArticlesViaHashtag(searchKeyword,pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(),articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "/articles/search-hashtag";
    }
    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "/articles/form";
    }

    @PostMapping ("/form")
    public String postNewArticle(
            ArticleRequest articleRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
                                 ) {
        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping ("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDTO.of(
                "Juns", "asdf1234", "juns@mail.com", "Juns", "memo"
        )));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
            ) {
       articleService.deleteArticle(articleId,boardPrincipal.getUsername());
        return "redirect:/articles";
    }
}
