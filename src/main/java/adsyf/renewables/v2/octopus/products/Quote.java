package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Quote {
    @JsonProperty("annual_cost_inc_vat")
    Integer annualCostIncVat;
    @JsonProperty("annual_cost_exc_vat")
    Integer annualCostExcVat;
}
