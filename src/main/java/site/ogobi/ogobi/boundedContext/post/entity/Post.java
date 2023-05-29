package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.List;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Post extends BaseEntity {

    private Category category;
    @Column(length = 20)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    private Member author;  //  작성한 멤버

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    @Builder
    public Post(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public enum Category {
        FREE,
        SHARING
    }
}