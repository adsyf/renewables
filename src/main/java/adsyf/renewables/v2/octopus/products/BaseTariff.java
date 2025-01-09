package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class BaseTariff {
    @JsonProperty("code")
    String code;
    @JsonProperty("dual_fuel_discount_exc_vat")
    BigDecimal dualFuelDiscountExcVat;
    @JsonProperty("dual_fuel_discount_inc_vat")
    BigDecimal dualFuelDiscountIncVat;
    @JsonProperty("exit_fees_exc_vat")
    Integer exitFeesExcVat;
    @JsonProperty("exit_fees_inc_vat")
    Integer exitFeesIncVat;
    @JsonProperty("exit_fees_type")
    String exitFeesType;
    @JsonProperty("links")
    Link[] links;
    @JsonProperty("online_discount_exc_vat")
    BigDecimal onlineDiscountExcVat;
    @JsonProperty("online_discount_inc_vat")
    BigDecimal onlineDiscountIncVat;
    @JsonProperty("standing_charge_exc_vat")
    BigDecimal standingChargeExcVat;
    @JsonProperty("standing_charge_inc_vat")
    BigDecimal standingChargeIncVat;
}
