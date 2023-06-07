package site.ogobi.ogobi.boundedContext.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.boundedContext.auth.entity.SignUp;
import site.ogobi.ogobi.boundedContext.auth.service.AuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }


    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("signupForm", new SignUp());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid SignUp signUp, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/auth/signup";
        }
        authService.signUp(signUp);
        return "redirect:/";
    }

}
