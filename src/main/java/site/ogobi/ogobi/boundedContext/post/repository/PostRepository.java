package site.ogobi.ogobi.boundedContext.post.repository;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByCategory(Post.Category category, Pageable pageable);
    @Modifying // Query 어노테이션에서 작성된 조회를 제외한 데이터의 변경이 있는 삽입, 수정, 삭제 쿼리 사용시 필요한 어노테이션
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int incleaseView(@Param("id") Long Id);
}