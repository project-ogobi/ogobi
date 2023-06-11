package site.ogobi.ogobi.boundedContext.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.ogobi.ogobi.boundedContext.member.entity.MemberTitle;

import java.util.Optional;

@Repository
public interface MemberTitleRepository extends JpaRepository<MemberTitle, Long> {

    @Override
    Optional<MemberTitle> findById(Long id);
}
