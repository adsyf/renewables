package adsyf.renewables.components;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface HybridInverter extends DcDcConverter, Inverter, Rectifier {
    public void operate(ZonedDateTime startTime, BigDecimal solarEnergyInDcKwh, BigDecimal timeHours, BigDecimal drawAcKwh);
}
