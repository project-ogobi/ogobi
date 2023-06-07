package site.ogobi.ogobi.boundedContext.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypages")
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String myHome(Model model){
        Member member = rq.getMember();

        List<Challenge> successChallengeList = memberService.successList(member);
        List<Challenge> runningChallengeList = memberService.runningList(member);

        if (successChallengeList==null){
            rq.historyBack("완료된 챌린지가 없습니다.");
        }

        if (runningChallengeList==null){
            rq.historyBack("진행 중인 챌린지가 없습니다.");
        }

        model.addAttribute("successList",successChallengeList);
        model.addAttribute("runningChallengeList",runningChallengeList);
        model.addAttribute("member", member);
        return "mypage/home";
    }


}
