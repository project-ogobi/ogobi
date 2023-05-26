package site.ogobi.ogobi.boundedContext.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
