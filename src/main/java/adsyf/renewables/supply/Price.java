package adsyf.renewables.supply;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class Price {
    private BigDecimal excVat;
    private BigDecimal incVat;
}
