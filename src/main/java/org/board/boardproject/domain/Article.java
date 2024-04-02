package org.board.boardproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
// 생성일시와 생성자 등 Auditing에 적용된 값도 Tostring으로 반환하겠다 라는 코드
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
// Spring Jpa framework로 자동 로드하기 위해서는 Entity 객체에도 어노테이션이 필요하다
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) @JoinColumn(name="userId") private UserAccount userAccount;

    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문
    @Setter
    private String hashtag; // 해시태그

    // 양뱡향 바인딩을 끊어주기 위해 Exclude를 지정
    @ToString.Exclude
    @OrderBy("createdAt DESC")
    // mapppedBy를 하지 않으면 article_article_comment로 생성되는 것을 aritcle테이블로 온것으로 mapping 하는 역할
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articlecomments = new LinkedHashSet<>();

    protected Article() {
    }

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // factory method로 접근하도록 설정
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    // 리스트, 컬렉션 (중복요소 체크) 또는 정렬
    // 동일성 동등성 검사 equals, hashcode로 비교 진행 -> 어노테이션을 사용하지 않고 필요한 값만 사용


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
