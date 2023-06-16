package site.ogobi.ogobi.boundedContext.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

import static jakarta.persistence.FetchType.LAZY;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
@Setter
public class GraphImage extends BaseEntity {

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    @ManyToOne(fetch = LAZY)
    private Challenge challenge;

}
