package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Products {
    @JsonProperty("count")
    Integer count;
    @JsonProperty("results")
    ProductsResult[] results;
    @JsonProperty("next")
    String next;
    @JsonProperty("previous")
    String previous;
}
