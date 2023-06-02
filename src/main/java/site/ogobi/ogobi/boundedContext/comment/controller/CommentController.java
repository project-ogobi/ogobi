package site.ogobi.ogobi.boundedContext.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.comment.dto.CommentDto;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.service.CommentService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final Rq rq;
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{category}/detail/{id}")
    public String createComment(Model model, @PathVariable String category, @PathVariable Long id, @Valid CommentDto commentDto, BindingResult bindingResult, Principal principal) {
        Post post = this.postService.getPost(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post/detail";
        }
        Member member = this.memberService.getMember(principal.getName());
        this.commentService.create(post, commentDto.getContent(), member);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

    @DeleteMapping("/{category}/{id}/detail/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable String category, @PathVariable Long id, @PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId).orElse(null);
        Member member = rq.getMember();

        if (!member.getUsername().equals(comment.getAuthor().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        commentService.deleteComment(comment);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/{id}/detail/{commentId}")
    public String modify(@PathVariable String category, @PathVariable Long id, @PathVariable Long commentId, @Valid CommentDto commentDto, BindingResult bindingResult) {
        Comment comment = commentService.findById(commentId).orElse(null);
        Member member = rq.getMember();

        if(bindingResult.hasErrors()) {
            return String.format("redirect:/posts/%s/detail/%s", category, id);
        }

        if (!member.getUsername().equals(comment.getAuthor().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        commentDto.setContent(comment.getContent());
        return String.format("redirect:/comments/%s/%s/detail/modify/%s", category, id, commentId);
    }

    @PostMapping("/{category}/{id}/detail/{commentId}/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyComment(@PathVariable String category, @PathVariable Long id, @PathVariable Long commentId, @Valid CommentDto commentDto, BindingResult bindingResult) {
        Comment comment = commentService.findById(commentId).orElse(null);
        Member member = rq.getMember();

        if(bindingResult.hasErrors()) {
            return String.format("redirect:/posts/%s/detail/%s", category, id);
        }

        if (!member.getUsername().equals(comment.getAuthor().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        commentService.modifyComment(comment, commentDto.getContent());

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

}