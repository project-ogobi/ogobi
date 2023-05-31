package site.ogobi.ogobi.boundedContext.spendingHistory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.file.entity.File;
import site.ogobi.ogobi.boundedContext.file.entity.FileDto;
import site.ogobi.ogobi.boundedContext.file.repository.FileRepository;
import site.ogobi.ogobi.boundedContext.file.service.FileService;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Slf4j
public class SpendingHistoryController {

    private final ChallengeService challengeService;
    private final SpendingHistoryService spendingHistoryService;
    private final FileService fileService;
    private final FileRepository fileRepository;


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
                                        @RequestParam List<MultipartFile> file) throws IOException {
        if (result.hasErrors()) {
            return "redirect:/challenges/" + challenge_id + "/" + sh_id + "/updateForm";
        }

        // FileRepository DB에 저장
        List<FileDto> imageFiles = fileService.uploadFiles(file);
        File newFile = File.builder()
                .itemName(form.getItemName())
                .itemPrice(form.getItemPrice())
                .description(form.getDescription())
                .imageFiles(imageFiles)
                .build();
        fileRepository.save(newFile);

        spendingHistoryService.updateSpendingHistory(form, sh_id, newFile.getId());

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

        // FileRepository DB에 저장
        List<FileDto> imageFiles = fileService.uploadFiles(file);
        File newFile = File.builder()
                .itemName(form.getItemName())
                .itemPrice(form.getItemPrice())
                .description(form.getDescription())
                .imageFiles(imageFiles)
                .build();
        fileRepository.save(newFile);

        spendingHistoryService.createSpendingHistory(form, challenge_id, newFile.getId());
        return "redirect:/challenges/" + challenge_id;
    }
}
