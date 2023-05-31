package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

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
<<<<<<< HEAD
    @OneToMany
    private Set<Like> like;
=======
    @ManyToMany
    private Set<Member> like;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
>>>>>>> d1b9d9e90a266e12a03a12ce18e1aec460eaa14c

    public enum Category {
        FREE,
        SHARING
    }
}