package site.ogobi.ogobi.boundedContext.file.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class File {

    private Long id;
    private String itemName;
    private int itemPrice;
    private String description;
    private List<FileDto> imageFiles;

    @Builder
    public File(String itemName, int itemPrice, String description, List<FileDto> imageFiles) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.description = description;
        this.imageFiles = imageFiles;
    }
}
