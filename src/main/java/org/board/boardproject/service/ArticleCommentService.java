package org.board.boardproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.board.boardproject.domain.Article;
import org.board.boardproject.domain.ArticleComment;
import org.board.boardproject.domain.UserAccount;
import org.board.boardproject.dto.ArticleCommentDTO;
import org.board.boardproject.repository.ArticleCommentRepository;
import org.board.boardproject.repository.ArticleRepository;
import org.board.boardproject.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    // 조회만 진행하는 것으로 트랜잭션 readonly 어노테이션 적용
    @Transactional(readOnly = true)
    public List<ArticleCommentDTO> searchArticleComments(Long articleId) {
        // 레포지토리는 엔티티 타입의 리스트로 전환
        List<ArticleComment> result = articleCommentRepository.findByArticle_id(articleId);
        // dto에서 구현한 from메소드로 엔티티를 dto로 변환하여 리스트에 추가 (Stream으로 리스트를 모두 확인)
        return result.stream().map(ArticleCommentDTO::from).toList();
    }

    // 댓글 생성
    public void saveArticleComment(ArticleCommentDTO articleCommentDTO) {
        try {
            Article article = articleRepository.getReferenceById(articleCommentDTO.articleId());
            UserAccount userAccount = userAccountRepository.getReferenceById(articleCommentDTO.userAccountDTO().userId());
            articleCommentRepository.save(articleCommentDTO.toEntity(article, userAccount));
        }catch(EntityNotFoundException e) {
            log.warn("댓글 저장 실패, 댓글 작성에 필요한 정보를 찾을 수 없습니다.");
        }
    }

    // 댓글 수정
    public void updateArticleComment(ArticleCommentDTO dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) {
                articleComment.setContent(dto.content());
            }
        }catch (EntityNotFoundException e) {
                log.warn("댓글 업데이트 실패, 댓글을 찾을 수 없습니다. - dto: {}", dto);
        }
    }
     // 댓글 삭제
    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }
}
