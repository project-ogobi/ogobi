package site.ogobi.ogobi.boundedContext.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.title.Title;

import java.util.List;

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
        List<Challenge> li = member.getChallenge();

        // 완료여부 중복체크
        for (Challenge challenge : li) {
            challengeService.checkDone(challenge.getId());
        }

        List<Challenge> doneChallengeList = memberService.doneList(member);
        List<Challenge> runningChallengeList = memberService.runningList(member);
        List<Title> memberTitles = memberService.titleList(member);
        String title = member.getTitle();

        if (doneChallengeList==null){
            rq.historyBack("완료된 챌린지가 없습니다.");
        }

        if (runningChallengeList==null){
            rq.historyBack("진행 중인 챌린지가 없습니다.");
        }

        if (memberTitles==null || title==null){
            rq.historyBack("획득한 칭호가 없습니다.");
        }

        model.addAttribute("doneList", doneChallengeList);
        model.addAttribute("runningChallengeList",runningChallengeList);
        model.addAttribute("memberTitles", memberTitles);
        model.addAttribute("member", member);

        return "mypage/home";
    }

    @GetMapping("/home/{title}")
    @PreAuthorize("isAuthenticated()")
    public String setTitle(@PathVariable String title){
        Member member = rq.getMember();

        memberService.setTitle(member, title);

        return "redirect:/mypages/home";
    }


}
