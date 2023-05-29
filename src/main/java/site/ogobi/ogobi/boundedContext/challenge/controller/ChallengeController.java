package site.ogobi.ogobi.boundedContext.challenge.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.form.CreateForm;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.List;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final Rq rq;
    private final ChallengeService challengeService;

    //challengeHome
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String home(Model model) {

        List<Challenge> li = rq.getMember().getChallenge();

        System.out.println(li);

        model.addAttribute("challenge", li);

        return "/challenge/challengeHome";
    }

    //createForm
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/createForm")
    public String goToCreate(Model model){
        model.addAttribute("createForm", new CreateForm());
        return "/challenge/createForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public String create(@Valid CreateForm createForm, BindingResult result){
        if (result.hasErrors()) {
            return "/challenge/createForm";
        }
        challengeService.create(rq.getMember(), createForm.getChallengeName(), createForm.getDescription(), createForm.getTargetMoney(), createForm.getStartDate(), createForm.getEndDate());
        return "redirect:/challenges";
    }

    @GetMapping("/detail") //나중에 challenge_id로 바꾸기
    public String showDetail(){

        return "/challenge/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}")
    public String showDetailById(@PathVariable Long challenge_id, Model model){
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();

        model.addAttribute("challenge", challenge);

        return "challenge/detail";
    }
}