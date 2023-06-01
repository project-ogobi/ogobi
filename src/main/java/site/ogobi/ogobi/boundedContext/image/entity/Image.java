package site.ogobi.ogobi.boundedContext.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

import static jakarta.persistence.FetchType.*;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Image extends BaseEntity {

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    @ManyToOne(fetch = LAZY)
    private SpendingHistory spendingHistory;

}
