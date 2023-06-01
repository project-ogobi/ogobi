package site.ogobi.ogobi.boundedContext.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import site.ogobi.ogobi.base.rq.Rq;
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
    private final Rq rq;
    private final CommentRepository commentRepository;

    public void create(Post post, String content, Member member) {
        Comment comment = Comment.builder()
                .post(post)
                .content(content)
                .author(member)
                .createDate(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Transactional
    public void modifyComment(Comment comment, String content) {
        if (comment==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글이 존재하지 않습니다.");
        }

        comment.setContent(content);

        this.commentRepository.save(comment);
    }
}
