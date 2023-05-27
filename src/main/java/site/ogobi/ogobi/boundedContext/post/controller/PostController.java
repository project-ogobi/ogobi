package site.ogobi.ogobi.boundedContext.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")  //글을 볼 수 있는지 권한확인, 로그인-> true / 로그아웃 -> false 반환
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
}