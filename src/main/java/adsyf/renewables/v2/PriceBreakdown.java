package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class PriceBreakdown {
    public enum Unit {
        kwh,day
    }
    BigDecimal pricePerUnit;
    Unit unit;
    BigDecimal unitQty;
    Period period;
    public BigDecimal getTotalPrice(){
        return this.pricePerUnit.multiply(unitQty);
    }
}
