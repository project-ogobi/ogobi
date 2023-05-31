package site.ogobi.ogobi.boundedContext.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.comment.dto.CommentDto;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.service.CommentService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final Rq rq;
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/{category}/detail/{id}")
    public String createComment(@PathVariable String category, @PathVariable Long id, @Valid CommentDto commentDto, BindingResult bindingResult) {
        Post post = this.postService.getPost(id);
        Member writer = rq.getMember();

        this.commentService.create(post, commentDto.getContent(), writer);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

    @GetMapping("/{category}/detail/{id}/{comment_id}")
    public String deleteComment(@PathVariable String category, @PathVariable Long id, @PathVariable Long comment_id) {
        Comment comment = commentService.findById(comment_id).orElse(null);
        Member member = rq.getMember();

        if (!commentService.isMyComment(member, comment)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        commentService.deleteComment(comment_id);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

    @PostMapping("/{category}/detail/{id}/modify/{comment_id}")
    public String modifyComment(@PathVariable String category, @PathVariable Long id, @PathVariable Long comment_id, String content) {
        Comment comment = commentService.findById(comment_id).orElse(null);
        Member member = rq.getMember();

        if (!commentService.isMyComment(member, comment)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentService.modifyComment(comment_id, content);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

}