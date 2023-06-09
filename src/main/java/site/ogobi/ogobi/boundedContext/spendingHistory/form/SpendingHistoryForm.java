package site.ogobi.ogobi.boundedContext.spendingHistory.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SpendingHistoryForm {

    @NotBlank
    private String itemName;

    @Min(0)
    private int itemPrice;

    @NotBlank
    private String description;

    private List<MultipartFile> imageFiles;

    @Builder
    public SpendingHistoryForm(String itemName, Integer itemPrice, String description) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.description = description;
    }
}
