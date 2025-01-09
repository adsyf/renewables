package adsyf.renewables.components;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface Battery extends ActiveElectronics {
    public BigDecimal charge(ZonedDateTime chargeStarttime, BigDecimal dcEnergyKwh, BigDecimal timeHours);
    public BigDecimal discharge(ZonedDateTime disChargeStarttime,BigDecimal dcEnergyKwh, BigDecimal timeHours);

    public BigDecimal gridCharge(ZonedDateTime chargeStarttime, BigDecimal acEnergyKwh, BigDecimal timeHours);
    public BigDecimal gridDischarge(ZonedDateTime disChargeStarttime,BigDecimal acEnergyKwh, BigDecimal timeHours);
    public BigDecimal getCapacityKwh();
    public BigDecimal getSocKwh();
    public BigDecimal getStorageEff();
}
