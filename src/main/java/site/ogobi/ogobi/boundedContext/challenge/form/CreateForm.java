package site.ogobi.ogobi.boundedContext.challenge.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class CreateForm{

    @NotBlank
    @Size(min=3, max=20)
    private String challengeName;

    @NotBlank
    private String description;

    @Min(0)
    private Integer targetMoney;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

}