package site.ogobi.ogobi.boundedContext.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
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
    @Autowired
    private MemberService memberService;

    private Member member1;

    @BeforeEach
    void setup() {
        member1 = memberService.join("user1", "1234", "첫번째멤버");
    }

    @Test
    @DisplayName("게시글 데이터")
    @Rollback(value = false)
    void test001() {

        Post p1 = Post.builder()
                .subject("제목1")
                .content("내용1")
                .category(Post.Category.FREE)
                .author(member1) // TODO: 이렇게 해도 author_id = null
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(p1);

        Post p2 = Post.builder()
                .subject("제목2")
                .content("내용2")
                .category(Post.Category.SHARING)
                .author(member1)
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
            this.postService.create(subject, content, Post.Category.SHARING, member1);
        }
    }
}