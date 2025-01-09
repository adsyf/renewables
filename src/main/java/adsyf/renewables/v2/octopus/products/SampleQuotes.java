package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SampleQuotes {
    @JsonProperty("_A")
    ElectricityTariffPaymentMethods a;
    @JsonProperty("_B")
    ElectricityTariffPaymentMethods b;
    @JsonProperty("_C")
    ElectricityTariffPaymentMethods c;
    @JsonProperty("_D")
    ElectricityTariffPaymentMethods d;
    @JsonProperty("_E")
    ElectricityTariffPaymentMethods e;
    @JsonProperty("_F")
    ElectricityTariffPaymentMethods f;
    @JsonProperty("_G")
    ElectricityTariffPaymentMethods g;
    @JsonProperty("_H")
    ElectricityTariffPaymentMethods h;
    @JsonProperty("_J")
    ElectricityTariffPaymentMethods j;
    @JsonProperty("_K")
    ElectricityTariffPaymentMethods k;
    @JsonProperty("_L")
    ElectricityTariffPaymentMethods l;
    @JsonProperty("_M")
    ElectricityTariffPaymentMethods m;
    @JsonProperty("_N")
    ElectricityTariffPaymentMethods n;
    @JsonProperty("_P")
    ElectricityTariffPaymentMethods p;
}
