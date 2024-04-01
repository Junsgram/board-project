package org.board.boardproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.constant.SearchType;
import org.board.boardproject.dto.ArticleDTO;
import org.board.boardproject.dto.ArticleWithCommentsDTO;
import org.board.boardproject.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Log4j2
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDTO::from);
        }
        // searchType 각 각 쿼리문 작성®
        return switch (searchType) {
            // mapping해서 dto로 변경 후 리턴
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDTO::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDTO::from);
            case ID ->
                    articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDTO::from);
            case NICKNAME ->
                    articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDTO::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDTO::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDTO getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDTO dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDTO dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            if (dto.title() != null) {
                article.setTitle(dto.title());
            }
            if (dto.content() != null) {
                article.setContent(dto.content());
            }
            article.setHashtag(dto.hashtag());
            articleRepository.save(article);
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패했습니다. 게시글을 찾을 수 없습니다 - dto : {} ", dto);
        }
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            // 해시태그 검색 전 빈페이지를 먼저 호출 할 예정
            return Page.empty(pageable);
        }
        // DTO로 값을 받아 뿌려준다 - view에 DTO로 받아 값을 출력하기 때문에
        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDTO::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
