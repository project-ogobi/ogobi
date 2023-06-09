package site.ogobi.ogobi.boundedContext.spendingHistory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Slf4j
public class SpendingHistoryController {

    private final ChallengeService challengeService;
    private final SpendingHistoryService spendingHistoryService;
    private final ImageService imageService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}/{sh_id}/updateForm")
    public String updateSpendingHistory(@PathVariable Long challenge_id, @PathVariable Long sh_id, Model model){
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        SpendingHistory spendingHistory = spendingHistoryService.findSpendingHistoryById(sh_id).orElseThrow();

        // dto 객체 전환
        SpendingHistoryForm spendingHistoryForm = spendingHistoryService.buildSpendingHistoryForm(spendingHistory);

        List<Image> Images = spendingHistory.getImageFiles();
        model.addAttribute("presentImages", Images);
        model.addAttribute("form", spendingHistoryForm);
        model.addAttribute("cid", challenge_id);
        model.addAttribute("sh_id", sh_id);

        return "challenge/updateSpendingHistory";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{challenge_id}/{sh_id}/updateForm")
    public String updateSpendingHistory(@Valid SpendingHistoryForm form, BindingResult result,
                                        @PathVariable Long challenge_id, @PathVariable Long sh_id,
                                        @RequestParam(value = "images", required = false) List<MultipartFile> file,
                                        @RequestParam(value = "existingImageId", required = false) List<String> listId ) throws IOException {

        if (file == null){
            spendingHistoryService.updateWithoutNewImage(form, sh_id, listId);
        }
        else{
            if (result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();

                for (ObjectError error : errors) {
                    String errorMessage = error.getDefaultMessage();

                    System.out.println("에러 발생! 메시지: " + errorMessage);
                }

                return "redirect:/challenges/" + challenge_id + "/" + sh_id + "/updateForm";
            }

            String filePath = challengeService.makeFilePathWithChallengeId(challenge_id);
            List<Image> imageFiles = imageService.uploadFiles(file, filePath);
            spendingHistoryService.updateSpendingHistory(form, sh_id, imageFiles, listId);
        }

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
                                        @RequestParam List<MultipartFile> file) throws IOException {
        if (result.hasErrors()) {
            return "redirect:/challenges/" + challenge_id + "/createForm";
        }

        String filePath = challengeService.makeFilePathWithChallengeId(challenge_id);
        List<Image> imageFiles = imageService.uploadFiles(file, filePath);
        spendingHistoryService.createSpendingHistory(form, challenge_id, imageFiles);
        return "redirect:/challenges/" + challenge_id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{challenge_id}/{sh_id}/delete")
    public String deleteSpendingHistory(@PathVariable Long challenge_id, @PathVariable Long sh_id){
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        SpendingHistory spendingHistory = spendingHistoryService.findSpendingHistoryById(sh_id).orElseThrow();
        challenge.updateUsedMoney(challenge.getUsedMoney() - spendingHistory.getPrice());

        spendingHistoryService.delete(sh_id);
        return "redirect:/challenges/" + challenge_id;
    }
}
