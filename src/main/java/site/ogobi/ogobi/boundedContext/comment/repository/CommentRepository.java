package site.ogobi.ogobi.boundedContext.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
