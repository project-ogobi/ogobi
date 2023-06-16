package site.ogobi.ogobi.boundedContext.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.title.Title;

import java.security.Principal;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{id}")
    public String showEditInformation(@PathVariable("id") Long memberId, Principal principal, Model model) {
        Member member = this.memberService.findById(memberId);
        if (!member.getUsername().equals(principal.getName())) {
            return rq.historyBack("접근할 수 없는 페이지입니다.");
        }
        model.addAttribute("member", member);

        return "mypage/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{id}")
    public String editInformation(@PathVariable("id") Long memberId, @RequestParam("nickname") String newNickname, Principal principal) {
        String trimNickname = newNickname.trim();

        if (memberService.findbyNickname(trimNickname) != null) {
            return rq.historyBack("이미 존재하는 닉네임입니다.");
        } else if (trimNickname.isEmpty()) {
            return rq.historyBack("닉네임을 입력해주세요.");
        } else if (trimNickname.length() < 2 || trimNickname.length() > 8) {
            return rq.historyBack("닉네임은 2자 이상, 8자 이하로 입력해주세요.");
        }
        memberService.editNickname(memberId, trimNickname, principal.getName());
        return "redirect:/mypages/home";
    }
}