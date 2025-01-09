package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Slf4j
public class Price {
    ArrayList<PriceBreakdown> priceBreakdowns;
    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (PriceBreakdown pb : priceBreakdowns) {
            total = total.add(pb.getTotalPrice());
        }
        return total;
    }
}
