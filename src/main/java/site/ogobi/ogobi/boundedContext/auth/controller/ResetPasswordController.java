package site.ogobi.ogobi.boundedContext.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.ogobi.ogobi.base.security.PasswordResetConfig;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class ResetPasswordController {

    private final MemberService memberService;
    private final PasswordResetConfig passwordResetConfig;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@ModelAttribute("memberId") String memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return passwordResetConfig.getPasswordResetProcessingPage();
    }

    @PostMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, @RequestParam("memberId") String memberId,
                                        RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("memberId", memberId);
            return "redirect:/" + passwordResetConfig.getPasswordResetProcessingPage() + "?error";
        }
        Member foundMember = memberService.findById(memberId);
        memberService.updateNewPassword(foundMember, password);

        return "redirect:/auth/login";
    }

}


