package site.ogobi.ogobi.boundedContext.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;


import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final Rq rq;
    private final PostService postService;

    @GetMapping("/detail/{id}")
//    @PreAuthorize("isAuthenticated()")  //글을 볼 수 있는지 권한확인, 로그인-> true / 로그아웃 -> false 반환
    public String showPost(Model model, @PathVariable Long id){
        Post post = postService.getPost(id);
        model.addAttribute("post", post);

        return "post/detail";

    }

    @GetMapping("/list")
    public String showList(Model model) {
        List<Post> postList = this.postService.getList();
        model.addAttribute("postList", postList);
        return "post/list";
    }

    @GetMapping("/create")
    public String showCreate() {
        return "post/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam String subject, @RequestParam String content) {
        // TODO 게시글을 저장한다.
        return "redirect:/posts/list"; // 저장 후 목록으로 이동
    }
}