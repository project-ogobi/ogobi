package site.ogobi.ogobi.boundedContext.auth.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        // Generate a password reset token and send it to the user's email
        // You can use libraries like JavaMail or Spring Mail to send emails
        emailService.sendResetTokenEmail(email, "123414");

        return "redirect:" + passwordResetConfig.getPasswordResetProcessingUrl();
    }
}
