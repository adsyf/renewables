package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ElectricityDualRate {
    @JsonProperty("electricity_day")
    Integer electricityDay;
    @JsonProperty("electricity_night")
    Integer electricityNight;
}
