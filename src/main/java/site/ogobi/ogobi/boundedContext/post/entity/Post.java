package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.like.entity.Like;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Column(length = 20)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Member author;  //  작성한 멤버

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;
  
    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private Set<Like> like;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public enum Category {
        FREE,
        SHARING
    }

    public void modify(String subject, String content) {
        this.subject = subject;
        this.content = content;
        this.modifyDate = LocalDateTime.now(); // BaseEntity protected 확인
    }
}