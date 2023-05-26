package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Post extends BaseEntity {

    private Category category;
    private String subject;
    private String content;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    public enum Category {
        FREE,
        SHARING
    }

    @Builder
    public Post (String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}