package site.ogobi.ogobi.boundedContext.post.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 데이터")
    @Rollback(false)
    void test001() {
        Post p1 = Post.builder()
                .subject("제목1")
                .content("내용1")
                .build();
        System.out.println(p1);

        postRepository.save(p1);

        Post p2 = Post.builder()
                .subject("제목2")
                .content("내용2")
                .build();

        postRepository.save(p2);
    }
}