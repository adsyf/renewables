package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DualFuelSingleRate {
    @JsonProperty("electricity_standard")
    Integer electricityStandard;
    @JsonProperty("gas_standard")
    Integer gasStandard;
}
