package site.ogobi.ogobi.boundedContext.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.bind.DefaultValue;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.image.entity.GraphImage;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Challenge extends BaseEntity {
    private String challengeName;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private int targetMoney; // 목표 금액
    private int usedMoney; // 현재까지 사용한 금액
    private int achievementRate; // 달성률
    @Setter
    private boolean success;    //  성공 여부

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "challenge", cascade = {CascadeType.ALL})
    private List<SpendingHistory> spendingHistories;

    @OneToMany(mappedBy = "challenge", cascade = {ALL})
    private List<GraphImage> graphImage;

    public boolean hasSpendingHistory(){
        return spendingHistories.size() != 0;
    }

    public boolean hasGraphImage() {
        return graphImage.size() != 0;
    }

    public void updateUsedMoney(int money){
        this.usedMoney = money;
    }

    public void updateAchievementRate(int achievementRate){
        this.achievementRate = achievementRate;
    }

    public void updateBasicInfo(String challengeName, String description, LocalDate startDate, LocalDate endDate, int targetMoney){
        this.challengeName = challengeName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetMoney = targetMoney;
    }
}
