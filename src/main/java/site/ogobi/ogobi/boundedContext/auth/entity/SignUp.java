package site.ogobi.ogobi.boundedContext.auth.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class SignUp {

    private String nickname;
    private String loginId;
    private String email;
    private String password;

    public SignUp(String nickname, String loginId, String email, String password) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
    }
}
