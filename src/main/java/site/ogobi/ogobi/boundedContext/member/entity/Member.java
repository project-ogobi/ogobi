package site.ogobi.ogobi.boundedContext.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.like.entity.Like;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends BaseEntity {

    private String nickname; // 닉네임
    @Column(unique = true)
    private String username; // 진짜 로그인할때 쓰는 아이디
    private String password;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    private List<Challenge> challenge;

    @OneToMany
    private Set<Like> like;     //  Todo 중복 제거.. 가능?

    public boolean isAdmin() {
        return "admin".equals(username);
    }

    public boolean hasChallenge(){
        return challenge.size() != 0;
    }

}
