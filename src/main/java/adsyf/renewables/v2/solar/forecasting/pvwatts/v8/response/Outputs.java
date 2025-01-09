package adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Outputs {
    @JsonProperty("ac_monthly")
    BigDecimal[] acMonthlyKWh;
    @JsonProperty("poa_monthly")
    BigDecimal[] poaMonthly;
    @JsonProperty("dc_monthly")
    BigDecimal[] dcMonthlyKWh;
    @JsonProperty("ac_annual")
    BigDecimal acAnnualKWh;
    @JsonProperty("solrad_annual")
    BigDecimal solRadAnnual;
    @JsonProperty("capacity_factor")
    BigDecimal capacityFactor;
    @JsonProperty("ac")
    BigDecimal[] acWh;
    @JsonProperty("poa")
    BigDecimal[] poa;
    @JsonProperty("dn")
    BigDecimal[] dn;
    @JsonProperty("dc")
    BigDecimal[] dcWh;
    @JsonProperty("df")
    BigDecimal[] df;
    @JsonProperty("tamb")
    BigDecimal[] tamb;
    @JsonProperty("tcell")
    BigDecimal[] tcell;
    @JsonProperty("wspd")
    BigDecimal[] wspd;
    @JsonProperty("alb")
    BigDecimal[] alb;
}
