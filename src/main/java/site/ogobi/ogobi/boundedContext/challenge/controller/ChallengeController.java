package site.ogobi.ogobi.boundedContext.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    //challengeHome
    @GetMapping("")
    public String home(){

        return "/challenge/challengeHome";
    }

    //createForm
    @GetMapping("/createForm")
    public String gotoCreateForm(){

        return "/challenge/createForm";
    }

    @GetMapping("/detail") //나중에 challenge_id로 바꾸기
    public String showDetail(){

        return "/challenge/detail";
    }

}