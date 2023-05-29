package site.ogobi.ogobi.boundedContext.spendingHistory;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;

@Entity
public class SpendingHistory extends BaseEntity {

    private String content; // 작성한 내용 ex)스타벅스커피...
    private int price; //각 사용금액 ex)4500원

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;
}
