package site.ogobi.ogobi.boundedContext.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.repository.CommentRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    public List<Comment> getCommentList(Long id) {
        Post post = postService.getPost(id);
        return post.getComments();
    }

    public boolean isMyComment(Member member, Comment comment) {   //댓글 작성자가 본인인지 여부 판단
        String writer = comment.getWriter().getNickname();

        if (member.getNickname().equals(writer)) {
            return true;
        }
        return false;
    }

    public void create(String content, Member writer) {
        Comment comment = Comment.builder()
                .content(content)
                .writer(writer)
                .createDate(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }
}
