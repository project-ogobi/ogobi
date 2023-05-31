package site.ogobi.ogobi.boundedContext.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.dto.PostDto;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.security.Principal;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{category}/detail/{id}")
    public String showPost(Model model, @PathVariable String category, @PathVariable Long id) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return category + "_post/detail";
    }

    @GetMapping("/{category}/list")
    public String showList(Model model, @PathVariable String category, @RequestParam(value = "page", defaultValue = "0") int page) {
        Post.Category postCategory = getCategory(category);
        Page<Post> paging = this.postService.getListByCategory(postCategory, page);
        model.addAttribute("paging", paging);
        return category + "_post/list";
    }

    @GetMapping("/{category}/create")
    public String showCreate(Model model, @PathVariable String category, PostDto postDto) {
        model.addAttribute("category", category);
        return category + "_post/create";
    }

    @PostMapping("/{category}/create")
    public String create(@Valid PostDto postDto, BindingResult bindingResult, @PathVariable String category) {
        if (bindingResult.hasErrors()) {
            return category + "_post/create";
        }
        Post.Category postCategory = getCategory(category);
        this.postService.create(postDto.getSubject(), postDto.getContent(), postCategory);
        return "redirect:/posts/" + category + "/list";
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
