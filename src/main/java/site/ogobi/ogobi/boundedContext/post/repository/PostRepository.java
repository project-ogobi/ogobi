package site.ogobi.ogobi.boundedContext.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
