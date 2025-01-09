package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class GasTariffs {
    @JsonProperty("_A")
    GasTariffPaymentMethods a;
    @JsonProperty("_B")
    GasTariffPaymentMethods b;
    @JsonProperty("_C")
    GasTariffPaymentMethods c;
    @JsonProperty("_D")
    GasTariffPaymentMethods d;
    @JsonProperty("_E")
    GasTariffPaymentMethods e;
    @JsonProperty("_F")
    GasTariffPaymentMethods f;
    @JsonProperty("_G")
    GasTariffPaymentMethods g;
    @JsonProperty("_H")
    GasTariffPaymentMethods h;
    @JsonProperty("_J")
    GasTariffPaymentMethods j;
    @JsonProperty("_K")
    GasTariffPaymentMethods k;
    @JsonProperty("_L")
    GasTariffPaymentMethods l;
    @JsonProperty("_M")
    GasTariffPaymentMethods m;
    @JsonProperty("_N")
    GasTariffPaymentMethods n;
    @JsonProperty("_P")
    GasTariffPaymentMethods p;
}
