package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SampleConsumption {
    @JsonProperty("electricity_single_rate")
    ElectricitySingleRate electricitySingleRate;
    @JsonProperty("electricity_dual_rate")
    ElectricityDualRate electricityDualRate;
    @JsonProperty("dual_fuel_single_rate")
    DualFuelSingleRate dualFuelSingleRate;
    @JsonProperty("dual_fuel_dual_rate")
    DuelFuelDualRate duelFuelDualRate;
}
