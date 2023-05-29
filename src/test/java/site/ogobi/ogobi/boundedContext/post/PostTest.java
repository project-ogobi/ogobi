package site.ogobi.ogobi.boundedContext.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.annotation.Rollback;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.PostRepository;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.time.LocalDateTime;


@SpringBootTest
class PostTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;


    @Test
    @DisplayName("게시글 데이터")
    @Rollback(false)
    void test001() {
        Post p1 = Post.builder()
                .subject("제목1")
                .content("내용1")
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(p1);

        Post p2 = Post.builder()
                .subject("제목2")
                .content("내용2")
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(p2);
    }

    @Test
    @DisplayName("대량 테스트 데이터")
    void test002() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용없음";
            this.postService.create(subject, content);
        }
    }
}