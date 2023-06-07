package site.ogobi.ogobi.boundedContext.auth.entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class SignUp {

    @Column(unique = true)
    @NotBlank
    private String nickname;

    @Column(unique = true)
    @NotBlank
    private String loginId;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Column(unique = true)
    @NotBlank
    private String password;

    public SignUp(String nickname, String loginId, String email, String password) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
    }
}
