package site.ogobi.ogobi.boundedContext.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.post.Post;

@Entity
public class Comment extends BaseEntity {

    private String content;

    @ManyToOne(fetch= FetchType.LAZY)
    private Post post;
}
