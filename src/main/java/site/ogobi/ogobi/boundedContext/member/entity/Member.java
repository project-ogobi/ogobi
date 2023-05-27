package site.ogobi.ogobi.boundedContext.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;

import java.util.List;

@Entity
public class Member extends BaseEntity {

    private String nickname; // 닉네임

    private String username; // 진짜 로그인할때 쓰는 아이디
    private String password;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    private List<Challenge> challenge;

    public boolean isAdmin() {
        return "admin".equals(username);
    }

}
