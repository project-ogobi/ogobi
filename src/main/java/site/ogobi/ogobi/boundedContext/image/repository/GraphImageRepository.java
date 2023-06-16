package site.ogobi.ogobi.boundedContext.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.ogobi.ogobi.boundedContext.image.entity.GraphImage;
import site.ogobi.ogobi.boundedContext.image.entity.Image;

import java.util.Optional;

@Repository
public interface GraphImageRepository extends JpaRepository<GraphImage, Long> {
    Optional<GraphImage> findByChallengeId(Long id);
}
