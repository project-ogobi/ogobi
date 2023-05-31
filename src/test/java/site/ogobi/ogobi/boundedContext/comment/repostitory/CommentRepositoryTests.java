package site.ogobi.ogobi.boundedContext.comment.repostitory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.repository.CommentRepository;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("댓글 데이터")
    @Rollback(value = false)
    void test001() {
        Optional<Post> op = this.postRepository.findById(1L);
        assertTrue(op.isPresent());
        Post post = op.get();

        Comment c1 = Comment.builder()
                .content("내용1")
                .post(post)
                .createDate(LocalDateTime.now())
                .build();
        commentRepository.save(c1);

        Comment c2 = Comment.builder()
                .content("내용2")
                .post(post)
                .createDate(LocalDateTime.now())
                .build();
        commentRepository.save(c2);
    }
}
