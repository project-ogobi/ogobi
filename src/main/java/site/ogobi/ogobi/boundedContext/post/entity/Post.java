package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.Comment;

import java.util.List;

@Entity
@Getter
public class Post extends BaseEntity {

    private Category category;
    private String subject;
    private String content;
    @OneToOne
    private String nickname;    //Todo 왜 에러가 생기는건지...?

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    public enum Category {
        FREE,
        SHARING
    }

}
