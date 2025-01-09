package adsyf.renewables.v2.octopus.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ListIndustryGridSupplyPointsResponse {
    @JsonProperty("count")
    Integer count;
    @JsonProperty("results")
    ListIndustryGridSupplyPointsResults[] results;
    @JsonProperty("next")
    String next;
    @JsonProperty("previous")
    String previous;
}
