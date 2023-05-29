package site.ogobi.ogobi.boundedContext.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("대량 테스트 데이터")
    void test001() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용없음";
            this.postService.create(subject, content);
        }
    }
}