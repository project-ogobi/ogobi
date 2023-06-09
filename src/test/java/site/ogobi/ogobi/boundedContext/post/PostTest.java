package site.ogobi.ogobi.boundedContext.post;

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

@SpringBootTest
class PostTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;

//    @Test
//    @DisplayName("게시글 데이터")
//    @Rollback(value = false)
//    void test001() {
//
//        Member member1 = memberService.getMember("user1");
//        Member member2 = memberService.getMember("test1");
//
//        postService.create("공유1", "내용", Post.Category.SHARING, member1);
//        postService.create("자유1", "내용", Post.Category.FREE, member1);
//
//        postService.create("공유2", "내용", Post.Category.SHARING, member2);
//        postService.create("자유2", "내용", Post.Category.FREE, member2);
//    }
//
//    @Test
//    @DisplayName("제목 20자 게시글")
//    void test002() {
//
//        postService.create("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요", "내용", Post.Category.SHARING, memberService.getMember("user1"));
//        postService.create("반가워요반가워요반가워요반가워요반가워요반가워요", "내용", Post.Category.SHARING, memberService.getMember("test1"));
//    }
//
//    @Test
//    @DisplayName("대량 테스트 데이터")
//    void test003() {
//        for (int i = 1; i <= 300; i++) {
//            String subject = String.format("테스트 데이터입니다:[%03d]", i);
//            String content = "내용없음";
//            this.postService.create(subject, content, Post.Category.SHARING, memberService.getMember("user1"));
//        }
//    }
}