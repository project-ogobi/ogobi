package site.ogobi.ogobi.boundedContext.comment.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.repository.CommentRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private CommentController commentController;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("댓글 삭제")
    @Rollback(value = false)
    @WithUserDetails("user1")
    void t01(){
        commentController.deleteComment("sharing", 1L, 1L);

        assertEquals(3, this.commentRepository.count());
    }
}
