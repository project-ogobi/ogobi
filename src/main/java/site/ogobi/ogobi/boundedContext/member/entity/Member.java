package site.ogobi.ogobi.boundedContext.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.chatRoom.ChatRoom;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends BaseEntity {

    private String nickname; // 닉네임
    @Column(unique = true)
    private String username; // 진짜 로그인할때 쓰는 아이디
    private String password;
    private String email;
    private String providerType; // 어떤 OAuth인지 (Local, Kakao, Google, Naver)
    @Setter
    private String title;   //  칭호
    private String resetToken = null;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    private List<Challenge> challenge;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.ALL})
    private List<ChatRoom> ownedChatRoom;

    public boolean isAdmin() {
        return "admin".equals(username);
    }

    public boolean hasChallenge() {
        return challenge.size() != 0;
    }

    // 스프링 시큐리티 규격
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 member 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // username이 admin이면 추가로 admin 권한을 가진다.
        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}