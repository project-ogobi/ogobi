package site.ogobi.ogobi.boundedContext.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.util.Optional;
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
}
