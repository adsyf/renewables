package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class Loss {
    private LossType lossType;
    private BigDecimal kwh;
}
