package site.ogobi.ogobi.boundedContext.like.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.like.service.LikeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.awt.*;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class LikeController {
    private Rq rq;
    private final LikeService likeService;
    private final PostService postService;
    @GetMapping("/{id}/like")
    public void like(Long id){
        Member member = rq.getMember();
        Post post = postService.findById(id).orElse(null);

        if (likeService.isLiked(id)) {
            likeService.createLike(member, post);
        }
    }


}
