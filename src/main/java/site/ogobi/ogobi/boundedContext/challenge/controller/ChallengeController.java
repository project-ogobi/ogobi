package site.ogobi.ogobi.boundedContext.challenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.form.CreateForm;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {

    private final Rq rq;
    private final ChallengeService challengeService;
    private final SpendingHistoryService spendingHistoryService;

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
        model.addAttribute("challenge", challenge);
        return "challenge/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}/spending-history")
    public String spendingHistory(@PathVariable Long challenge_id, Model model){
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        model.addAttribute("challenge", challenge);
        return "challenge/spendingHistory";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}/{sh_id}/updateForm")
    public String updateSpendingHistory(@PathVariable Long challenge_id, @PathVariable Long sh_id, Model model){
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        SpendingHistory spendingHistory = spendingHistoryService.findSpendingHistoryById(sh_id).orElseThrow();
        // dto 객체 전환
        SpendingHistoryForm spendingHistoryForm = SpendingHistoryForm.builder()
                .itemName(spendingHistory.getContent())
                .itemPrice(spendingHistory.getPrice())
                .build();

        log.info("spendingHistoryForm={}", spendingHistoryForm);
        model.addAttribute("form", spendingHistoryForm);
        model.addAttribute("cid", challenge_id);
        model.addAttribute("sh_id", sh_id);

        return "challenge/updateSpendingHistory";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{challenge_id}/{sh_id}/updateForm")
    public String updateSpendingHistory(@Valid SpendingHistoryForm form, BindingResult result,
                                        @PathVariable Long challenge_id, @PathVariable Long sh_id,
                                        @RequestParam MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            return "redirect:/challenges/" + challenge_id + "/" + sh_id + "/updateForm";
        }
        spendingHistoryService.updateSpendingHistory(form, sh_id);
        return "redirect:/challenges/" + challenge_id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}/createForm")
    public String createSpendingHistory(Model model, @PathVariable Long challenge_id){
        model.addAttribute("form", new SpendingHistoryForm());
        return "challenge/createSpendingHistory";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{challenge_id}/createForm")
    public String createSpendingHistory(@Valid SpendingHistoryForm form, BindingResult result,
                                        @PathVariable Long challenge_id,
                                        @RequestParam MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            return "redirect:/challenges/" + challenge_id + "/createForm";
        }
        spendingHistoryService.createSpendingHistory(form, challenge_id);
        return "redirect:/challenges/" + challenge_id;
    }

}
