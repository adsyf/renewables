package adsyf.renewables.supply;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class SolarInverter {
    private final static BigDecimal DEFAULT_SOLAR_TO_AC_EFF = new BigDecimal("96");
    private BigDecimal maxDcKW;
    private BigDecimal maxSolarToAcEff;
    private BigDecimal europeanEfficiency;
    private BigDecimal maxAcKW;
    private BigDecimal minPptVolts;
    private BigDecimal maxPptVolts;
    private int mpptCount;
    private BigDecimal maxDcAmps;
}
