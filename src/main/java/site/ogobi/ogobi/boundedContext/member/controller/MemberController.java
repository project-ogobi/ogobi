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
import site.ogobi.ogobi.boundedContext.member.entity.MemberTitle;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.title.Title;

import java.util.List;
import java.util.Optional;

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
        List<Title> memberTitles = memberService.titleList(member);

        if (successChallengeList==null){
            rq.historyBack("완료된 챌린지가 없습니다.");
        }

        if (runningChallengeList==null){
            rq.historyBack("진행 중인 챌린지가 없습니다.");
        }

        if (memberTitles==null){
            rq.historyBack("획득한 칭호가 없습니다.");
        }

        model.addAttribute("successList",successChallengeList);
        model.addAttribute("runningChallengeList",runningChallengeList);
        model.addAttribute("memberTitles", memberTitles);
        model.addAttribute("member", member);
        return "mypage/home";
    }


}
