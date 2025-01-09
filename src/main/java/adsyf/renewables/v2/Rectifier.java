package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class Rectifier {
    private BigDecimal efficiency;
    private BigDecimal contDcKw;
    private BigDecimal peakDcKw;
    final static BigDecimal DFT_EFFICIENCY=new BigDecimal("0.96");

    public EnergyEvent getEvent(Period period){
        EnergyEvent ee = new EnergyEvent();
        return ee;
    }

}