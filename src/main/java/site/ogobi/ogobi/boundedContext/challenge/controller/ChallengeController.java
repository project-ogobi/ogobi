package site.ogobi.ogobi.boundedContext.challenge.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    @AllArgsConstructor
    @Getter
    public static class CreateForm{

        @NotBlank
        @Size(min=3, max=20)
        private final String challengeName;

        @NotBlank
        private final String description;

        @NotBlank
        private final int targetMoney;

    }

    //createForm
    @GetMapping("/createForm")
    public String gotoCreateForm(@Valid CreateForm createForm){
        challengeService.create(createForm.getChallengeName(), createForm.getDescription(), createForm.getTargetMoney());

        return "/challenge/createForm";
    }

    @GetMapping("/detail") //나중에 challenge_id로 바꾸기
    public String showDetail(){

        return "/challenge/detail";
    }

}