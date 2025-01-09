package adsyf.renewables.supply;

import java.math.BigDecimal;

public class HybridSolarInverter  extends SolarInverter {
    private final static BigDecimal DEFAULT_SOLAR_TO_DC_EFF = new BigDecimal("99");
    private BigDecimal maxSolarToDcEff;
    private BigDecimal maxDischargeKw;
    private BigDecimal maxChargeKw;
}
