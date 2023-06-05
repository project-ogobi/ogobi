package site.ogobi.ogobi.boundedContext.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypages")
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final ChallengeService challengeService;

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String myHome(Model model){
        Member member = rq.getMember();

        model.addAttribute("member", member);
        return "mypage/home";
    }

}
