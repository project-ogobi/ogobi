package site.ogobi.ogobi.boundedContext.goodPlace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListPriceModelStoreService {
    @JsonProperty("list_total_count")
    private String total;

    @JsonProperty("row")
    private List<StoreData> storeData;
}
