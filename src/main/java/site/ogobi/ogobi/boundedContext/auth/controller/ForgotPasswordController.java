package site.ogobi.ogobi.boundedContext.auth.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.ogobi.ogobi.base.security.PasswordResetConfig;
import site.ogobi.ogobi.boundedContext.auth.service.EmailService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class ForgotPasswordController {

    private final MemberService memberService;
    private final PasswordResetConfig passwordResetConfig;
    private final EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return passwordResetConfig.getPasswordResetPage();
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email) throws MessagingException {
        if (memberService.findByEmail(email) == null) {
            return "redirect:" + passwordResetConfig.getPasswordResetPageUrl() + "?error";
        }

        String resetToken = emailService.generateResetToken(8);
        emailService.sendResetTokenEmail(email, resetToken);
        emailService.setMemberResetToken(resetToken, email);

        return "redirect:/" + passwordResetConfig.getPasswordResetTokenPage();
    }

    @GetMapping("/reset-token")
    public String showResetTokenPage() {
        return passwordResetConfig.getPasswordResetTokenPage();
    }

    @PostMapping("/reset-token")
    public String validateResetToken(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        Member user = memberService.findByResetToken(token);
        if (user == null) {
            return "redirect:/" + passwordResetConfig.getPasswordResetTokenPage() + "?error";
        }
        redirectAttributes.addFlashAttribute("memberId", user.getId());

        return "redirect:" + passwordResetConfig.getPasswordResetProcessingUrl();

    }
}
