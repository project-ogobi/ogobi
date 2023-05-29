package site.ogobi.ogobi.boundedContext.spendingHistory.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpendingHistoryForm {

    @NotBlank
    private String itemName;

    @Min(0)
    private String itemPrice;

    @NotBlank
    private String description;

}
