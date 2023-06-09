package site.ogobi.ogobi.boundedContext.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.post.entity.ChallengePost;

import java.util.Optional;

public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long> {
    Optional<ChallengePost> findByPostId(Long id);
}
