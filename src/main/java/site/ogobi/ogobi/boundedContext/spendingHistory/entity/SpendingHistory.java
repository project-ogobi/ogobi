package site.ogobi.ogobi.boundedContext.spendingHistory.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SpendingHistory extends BaseEntity {

    private String content; // 작성한 내용 ex)스타벅스커피...
    private int price; //각 사용금액 ex)4500원
    private LocalDateTime date;
    private String description;

    private Long fileId;

    @ManyToOne(fetch= FetchType.LAZY)
    private Challenge challenge;

    public void update(String content, String description, Long fileId) {
        this.content = content;
        this.description = description;
        this.fileId = fileId;
    }
}
