package site.ogobi.ogobi.boundedContext.challenge.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.member.Member;
import site.ogobi.ogobi.boundedContext.spendingHistory.SpendingHistory;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity
public class Challenge extends BaseEntity {
    private String challengeName;
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int targetMoney; // 목표 금액
    private int usedMoney; // 총 사용금액?
    private int achievementRate; // 달성률

    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "challenge", cascade = {CascadeType.ALL})
    private List<SpendingHistory> spendingHistories;

}
