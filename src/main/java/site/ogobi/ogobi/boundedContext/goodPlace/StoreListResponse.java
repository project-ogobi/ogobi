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
public class StoreListResponse {
    @JsonProperty("ListPriceModelStoreService")
    private ListPriceModelStoreService service;
}
