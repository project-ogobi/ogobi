package site.ogobi.ogobi.boundedContext.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;

import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {

    private String nickname; // 닉네임
    @Column(unique = true)
    private String username; // 진짜 로그인할때 쓰는 아이디
    private String password;

//    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
//    @OrderBy("id desc")
//    private List<Post> post;  //Todo post list도 필요할 것 같은데 mappedBy가 같아서 그런지 에러 발생

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    private List<Challenge> challenge;

    public boolean isAdmin() {
        return "admin".equals(username);
    }

}
