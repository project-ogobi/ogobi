package site.ogobi.ogobi.boundedContext.goodPlace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreData {
    @JsonProperty("SH_ID")
    private Long id;
    @JsonProperty("SH_NAME")
    private String name; // 가게 이름
    @JsonProperty("INDUTY_CODE_SE")
    private int categoryCode; // 분류 코드
    @JsonProperty("INDUTY_CODE_SE_NAME")
    private String categoryName;
    @JsonProperty("SH_ADDR")
    private String address;
    @JsonProperty("SH_PHONE")
    private String phoneNumber;
    @JsonProperty("SH_PHOTO")
    private String photo;
    @JsonProperty("SH_RCMN")
    private int recommend; // 추천수
}
