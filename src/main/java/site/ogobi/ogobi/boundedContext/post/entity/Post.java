package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.Comment;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends BaseEntity {

    private Category category;
    private String subject;
    private String content;
    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    public enum Category {
        FREE,
        SHARING
    }
}