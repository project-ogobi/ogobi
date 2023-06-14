package site.ogobi.ogobi.boundedContext.spendingHistory.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    @NotNull
    private LocalDate date; // 지출이 일어난 날짜

    private List<MultipartFile> imageFiles;

    @Builder
    public SpendingHistoryForm(String itemName, Integer itemPrice, String description, LocalDate date) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.description = description;
        this.date = date;
    }
}
