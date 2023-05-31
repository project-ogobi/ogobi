package site.ogobi.ogobi.boundedContext.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.boundedContext.post.dto.PostDto;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

@Controller
@RequestMapping("/posts/sharing")
@RequiredArgsConstructor
public class SharingPostController {

    private final PostService postService;

    @GetMapping("/detail/{id}")
//    @PreAuthorize("isAuthenticated()")  //글을 볼 수 있는지 권한확인, 로그인-> true / 로그아웃 -> false 반환
    public String showPost(Model model, @PathVariable Long id) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);

        return "sharing_post/detail";

    }

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = this.postService.getListByCategory(Post.Category.SHARING, page);
        model.addAttribute("paging", paging);
        return "sharing_post/list";
    }

    @GetMapping("/create")
    public String showCreate(PostDto postDto) {
        return "sharing_post/create";
    }

    @PostMapping("/create")
    public String create(@Valid PostDto postDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sharing_post/create";
        }
        this.postService.create(postDto.getSubject(), postDto.getContent(), Post.Category.SHARING);
        return "redirect:/posts/sharing/list"; // 저장 후 목록으로 이동
    }
}
