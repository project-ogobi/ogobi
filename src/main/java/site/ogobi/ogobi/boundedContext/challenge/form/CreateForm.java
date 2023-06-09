package site.ogobi.ogobi.boundedContext.challenge.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class CreateForm{

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min=3, max=20, message = "제목은 3자 이상, 20자 이하로 입력해주세요.")
    private String challengeName;

    @NotBlank(message = "내용을 입력해주세요.")
    private String description;

    @NotNull(message = "목표 금액을 숫자로만 입력해주세요.")
    @Min(0)
    private Integer targetMoney;

    @NotNull(message = "시작 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public void formBuilder(String challengeName, String description, Integer targetMoney, LocalDate startDate, LocalDate endDate) {
        this.challengeName = challengeName;
        this.description = description;
        this.targetMoney = targetMoney;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}