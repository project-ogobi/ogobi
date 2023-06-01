package site.ogobi.ogobi.boundedContext.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.boundedContext.comment.dto.CommentDto;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.post.dto.PostDto;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.security.Principal;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/{category}/detail/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showPost(Model model, @PathVariable String category, @PathVariable Long id, CommentDto commentDto) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "/post/detail";
    }

    @GetMapping("/{category}/list")
    public String showList(Model model, @PathVariable String category, @RequestParam(value = "page", defaultValue = "0") int page) {
        Post.Category postCategory = getCategory(category);
        Page<Post> paging = this.postService.getListByCategory(postCategory, page);
        model.addAttribute("paging", paging);
        return "/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/create")
    public String showCreate(Model model, @PathVariable String category, PostDto postDto) {
        model.addAttribute("category", category);
        return "/post/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{category}/create")
    public String create(@Valid PostDto postDto, BindingResult bindingResult, @PathVariable String category, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/post/create";
        }
        Post.Category postCategory = getCategory(category);
        Member member = this.memberService.getMember(principal.getName());
        this.postService.create(postDto.getSubject(), postDto.getContent(), postCategory, member);
        return String.format("redirect:/posts/%s/list", category);
    }

    private Post.Category getCategory(String category) {
        if ("free".equals(category)) {
            return Post.Category.FREE;
        } else if ("sharing".equals(category)) {
            return Post.Category.SHARING;
        }
        throw new IllegalArgumentException("Invalid category: " + category);
    }
}