package site.ogobi.ogobi.boundedContext.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.like.service.LikeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class LikeController {

    private final Rq rq;
    private final LikeService likeService;
    private final PostService postService;
    private final MemberService memberService;


    @PostMapping("/{category}/detail/{id}/like")
    public String like(@PathVariable String category, @PathVariable Long id, Principal principal){
        String memberName = principal.getName();
        Member member = memberService.findByUsername(memberName).orElse(null);
        Post post = postService.findById(id).orElse(null);

        if(member.getUsername().equals(post.getAuthor().getUsername())){    //본인 글에는 추천을 할 수 없다.
            return rq.historyBack("본인의 글은 추천할 수 없습니다.");
        }

        this.likeService.createLike(member, post);

        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }
}
