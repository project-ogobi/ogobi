package site.ogobi.ogobi.base.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfig {

    private String passwordResetPage = "auth/forgot-password";
    private String passwordResetPageUrl = "/auth/forgot-password";
    private String passwordResetProcessingPage = "auth/reset-password";
    private String passwordResetProcessingUrl = "/auth/reset-password";
    private String passwordResetSuccessUrl = "auth/login?reset";

}
