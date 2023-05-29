package site.ogobi.ogobi.boundedContext.spendingHistory.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpendingHistoryForm {

    @NotBlank
    private String itemName;

    @Min(0)
    private int itemPrice;

    @NotBlank
    private String description;

    @Builder
    public SpendingHistoryForm(String itemName, Integer itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
