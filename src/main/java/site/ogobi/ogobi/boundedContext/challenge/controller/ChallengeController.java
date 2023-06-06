package site.ogobi.ogobi.boundedContext.challenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.form.CreateForm;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {

    private final Rq rq;
    private final ChallengeService challengeService;

    //challengeHome
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String home(Model model) {
        List<Challenge> li = rq.getMember().getChallenge();
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
        if(!Objects.equals(rq.getMember().getId(), challenge.getMember().getId())){
            return "error";
        }

        model.addAttribute("challenge", challenge);
        return "challenge/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/update")
    public String goToUpdate(Model model, @PathVariable Long id){

        Challenge c = challengeService.findChallengeById(id).orElseThrow();

        if(!Objects.equals(rq.getMember().getId(), c.getMember().getId())){
            return "error";
        }

        CreateForm updateForm = new CreateForm();
        updateForm.formBuilder(c.getChallengeName(),
                c.getDescription(),
                c.getTargetMoney(),
                c.getStartDate(),
                c.getEndDate());

        model.addAttribute("updateForm", updateForm);
        model.addAttribute("updateId", id);
        return "/challenge/updateForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public String update(@Valid CreateForm updateForm, @PathVariable Long id, BindingResult result){
        if (result.hasErrors()) {
            return "/challenge/updateForm";
        }
        if(!challengeService.canUpdate(rq.getMember(), id)){
            return "";//"올바르지 않은 접근 처리";
        }
        challengeService.update(rq.getMember(), updateForm, id);
        return "redirect:/challenges";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id){
        //TODO: 정말 삭제하시겠습니까? 추가하기

        challengeService.deleteById(rq.getMember(), id);
        return "redirect:/challenges";
    }


}
