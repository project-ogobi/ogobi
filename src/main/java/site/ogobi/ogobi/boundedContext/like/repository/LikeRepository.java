package site.ogobi.ogobi.boundedContext.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByMember(Member member);
}
