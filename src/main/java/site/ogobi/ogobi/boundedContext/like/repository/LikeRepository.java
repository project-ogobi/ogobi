package site.ogobi.ogobi.boundedContext.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.like.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
