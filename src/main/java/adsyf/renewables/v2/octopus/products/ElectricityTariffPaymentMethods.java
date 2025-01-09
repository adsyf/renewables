package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ElectricityTariffPaymentMethods {
    @JsonProperty("direct_debit_monthly")
    ElectricityTariff directDebitMonthly;
    @JsonProperty("direct_debit_quarterly")
    ElectricityTariff directDebitQuarterly;
    @JsonProperty("porob")
    ElectricityTariff porob;
    @JsonProperty("prepayment")
    ElectricityTariff prepayment;
    @JsonProperty("varying")
    ElectricityTariff varying;
}
