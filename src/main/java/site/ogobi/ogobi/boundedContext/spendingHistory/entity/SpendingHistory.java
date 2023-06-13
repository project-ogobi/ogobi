package site.ogobi.ogobi.boundedContext.spendingHistory.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.image.entity.Image;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SpendingHistory extends BaseEntity {

    private String content; // 지출내역 품목
    private int price; //각 사용금액 ex)4500원
    private String description; // 작성한 내용 ex)스타벅스커피...
    private LocalDate date; // 실제 지출이 일어난 날짜

    @OneToMany(mappedBy = "spendingHistory", cascade = {ALL})
    private List<Image> imageFiles = new ArrayList<>();

    @ManyToOne(fetch= FetchType.LAZY)
    private Challenge challenge;

}
