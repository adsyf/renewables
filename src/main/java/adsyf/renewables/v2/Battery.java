package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Slf4j
public class Battery {
    private BigDecimal maxChargeKw;
    private BigDecimal maxDischargeKw;
    private BigDecimal chargeEfficiency;
    private BigDecimal dischargeEfficiency;
    final static BigDecimal DFT_EFFICIENCY=new BigDecimal("0.93");

    public BigDecimal getChargeEfficiency(){
        return Objects.requireNonNullElse(this.chargeEfficiency, Battery.DFT_EFFICIENCY);
    }
    public BigDecimal getDischargeEfficiency(){
        return Objects.requireNonNullElse(this.dischargeEfficiency, Battery.DFT_EFFICIENCY);
    }

    EnergyEvent charge(Period period, BigDecimal kwhIn){
        EnergyEvent ee = new EnergyEvent();
        return ee;
    }
    EnergyEvent discharge(Period period, BigDecimal kwhIn){
        EnergyEvent ee = new EnergyEvent();
        return ee;
    }



}
