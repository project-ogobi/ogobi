package site.ogobi.ogobi.boundedContext.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.ogobi.ogobi.base.security.PasswordResetConfig;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class ResetPasswordController {

    private final MemberService memberService;
    private final PasswordResetConfig passwordResetConfig;

    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return passwordResetConfig.getPasswordResetProcessingPage();
    }

    @PostMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        log.info("Reset view!");

//        Member user = memberService.findByResetToken(token);
//
//        if (user == null) {
//            // Handle invalid or expired token
//            return "redirect:" + passwordResetConfig.getPasswordResetPage() + "?error";
//        }
//
//        model.addAttribute("token", token);
        return "reset-password";
    }

}


