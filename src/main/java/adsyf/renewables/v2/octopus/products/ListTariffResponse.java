package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ListTariffResponse {
    @JsonProperty("count")
    Integer count;
    @JsonProperty("results")
    HistoricalCharge[] results;
    @JsonProperty("next")
    String next;
    @JsonProperty("previous")
    String previous;
}
