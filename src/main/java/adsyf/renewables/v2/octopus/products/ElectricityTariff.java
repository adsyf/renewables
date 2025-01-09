package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
@Data
@Slf4j
public class ElectricityTariff extends BaseTariff {
    @JsonProperty("day_unit_rate_exc_vat")
    BigDecimal dayUnitRateExcVat;
    @JsonProperty("day_unit_rate_inc_vat")
    BigDecimal dayUnitRateIncVat;
    @JsonProperty("night_unit_rate_exc_vat")
    BigDecimal nightUnitRateExcVat;
    @JsonProperty("night_unit_rate_inc_vat")
    BigDecimal nightUnitRateIncVat;
    @JsonProperty("standard_unit_rate_exc_vat")
    BigDecimal standardUnitRateExcVat;
    @JsonProperty("standard_unit_rate_inc_vat")
    BigDecimal standardUnitRateIncVat;
}
