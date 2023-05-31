package site.ogobi.ogobi.boundedContext.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
public class Challenge extends BaseEntity {
    private String challengeName;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private int targetMoney; // 목표 금액
    private int usedMoney; // 총 사용금액?
    private int achievementRate; // 달성률

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "challenge", cascade = {CascadeType.ALL})
    private List<SpendingHistory> spendingHistories;

    public boolean hasSpendingHistory(){
        return spendingHistories.size() != 0;
    }

    public void updateUsedMoney(int money){
        this.usedMoney = money;
    }

    public void updateAchievementRate(int achievementRate){
        this.achievementRate = achievementRate;
    }

}
