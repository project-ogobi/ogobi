package site.ogobi.ogobi.boundedContext.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.comment.dto.CommentDto;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.comment.service.CommentService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final Rq rq;
    private final CommentService commentService;
    private final PostService postService;


    @PostMapping("/detail/{id}")
    public String createComment(@PathVariable Long id, @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/create";
        }

        Post post = this.postService.getPost(id);
        Member writer = rq.getMember();

        this.commentService.create(commentDto.getContent(), writer);

        return "post/detail";
    }

    @GetMapping("/{id}")    //  Todo url 수정해야 됨
    public String showComments(Model model, @PathVariable Long id) {
        List<Comment> commentList = commentService.getCommentList(id);

        model.addAttribute("comment", commentList);

        return "post/detail";

    }

    @GetMapping("/detail/{id}/{comment_id}") // Todo delete로 바꾸기
    public void deleteComment(@PathVariable Long id, @PathVariable Long comment_id){

    }


}
