package site.ogobi.ogobi.boundedContext.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.ogobi.ogobi.boundedContext.image.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
