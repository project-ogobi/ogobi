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
//import site.ogobi.ogobi.boundedContext.file.UploadFile;

import java.time.LocalDateTime;
import java.util.List;

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
//    private UploadFile imageFile;

    private String attachmentPath;

    @ManyToOne(fetch= FetchType.LAZY)
    private Challenge challenge;

    public void update(String content, int price, String description) {
        this.content = content;
        this.price = price;
        this.description = description;
    }
}
