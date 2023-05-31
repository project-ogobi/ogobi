package site.ogobi.ogobi.boundedContext.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    private Member writer;
    @ManyToOne
    private Post post;

}
