package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.Comment;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends BaseEntity {

    private Category category;
    private String subject;
    private String content;
    @ManyToOne
    private Member author;  //  작성한 멤버

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    public enum Category {
        FREE,
        SHARING
    }
}