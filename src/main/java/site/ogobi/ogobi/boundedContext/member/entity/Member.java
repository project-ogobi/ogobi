package site.ogobi.ogobi.boundedContext.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import jakarta.persistence.*;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

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
