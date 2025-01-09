package adsyf.renewables.v2.octopus.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ListIndustryGridSupplyPointsResults {
    @JsonProperty("group_id")
    String groupId;
}
