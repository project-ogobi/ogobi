package site.ogobi.ogobi.boundedContext.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypages")
public class MemberController {

    private final Rq rq;
    private final ChallengeService challengeService;

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String myHome(Model model){
        Member member = rq.getMember();

        model.addAttribute("member", member);
        return "mypage/home";
    }

    @GetMapping("/success")
    public String succeedChallenge(Model model){
        Member member = rq.getMember();
        List<Challenge> challengeList = challengeService.findByMember(member);
        List<Challenge> successList = new ArrayList<>();

        for (Challenge ch:challengeList) {
            if (ch.isSuccess()){
                successList.add(ch);
            }
        }

        if (successList.size()==0){
            return rq.historyBack("완료된 챌린지가 없습니다.");
        }

        model.addAttribute(successList);
        return "mypage/home";
    }

}
