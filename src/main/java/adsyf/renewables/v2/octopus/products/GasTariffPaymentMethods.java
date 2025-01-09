package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class GasTariffPaymentMethods {
    @JsonProperty("direct_debit_monthly")
    GasTariff directDebitMonthly;
    @JsonProperty("direct_debit_quarterly")
    GasTariff directDebitQuarterly;
    @JsonProperty("porob")
    GasTariff porob;
    @JsonProperty("prepayment")
    GasTariff prepayment;
    @JsonProperty("varying")
    GasTariff varying;
}
