package adsyf.renewables.installation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
@Slf4j
public class SolarDcToAcInverter {
    private final BigDecimal C = new BigDecimal(100);
    private final MathContext V = new MathContext(5);
    private String brand;
    private String model;
    private BigDecimal maxPvPowerKw;
    private BigDecimal startVoltage;
    private BigDecimal maxPvVoltage;
    private BigDecimal PvVoltageStart;
    private BigDecimal PvVoltageEnd;
    private BigDecimal MpptVoltageStart;
    private BigDecimal MpptVoltageEnd;
    private Integer NoOfMPPTrackers;
    private Integer stringsPerTracker;
    private BigDecimal contACPowerKw;
    private BigDecimal maxACPowerKw;
    private BigDecimal efficiency;
    private BigDecimal contACCurrentKw;
    private BigDecimal maxACCurrentKw;
    private final MathContext mc = new MathContext(5);

    public BigDecimal convertKwh(BigDecimal dcKwh,BigDecimal timeHours){
        BigDecimal avgRateOfPower = dcKwh.divide(timeHours,mc);
        if (avgRateOfPower.compareTo(this.maxPvPowerKw) > 0){
            throw new UnsupportedOperationException("Avg power: " + avgRateOfPower + ", max allowed: " + this.maxPvPowerKw + ".  Too much power inverter exploded.");
        }
        BigDecimal acKwh = dcKwh.multiply(this.efficiency.divide(C,V));
        if (acKwh.compareTo(contACPowerKw) > 0){
            return contACPowerKw;
        } else {
            return acKwh;
        }
    }
    public static void main(String[] args){
        SolarDcToAcInverter inv = new SolarDcToAcInverter();
        inv.setBrand("Solax");
        inv.setModel("X1-3.0T");
        inv.setMaxPvPowerKw(new BigDecimal("4.5"));
        inv.setStartVoltage(new BigDecimal(100));
        inv.setMaxPvVoltage(new BigDecimal(600));
        inv.setPvVoltageStart(new BigDecimal(360));
        inv.setPvVoltageEnd(new BigDecimal(600));
        inv.setMpptVoltageStart(new BigDecimal(70));
        inv.setMpptVoltageEnd(new BigDecimal(580));
        inv.setNoOfMPPTrackers(2);
        inv.setStringsPerTracker(1);
        inv.setContACPowerKw(new BigDecimal(3));
        inv.setMaxACPowerKw(new BigDecimal("3.3"));
        inv.setEfficiency(new BigDecimal("97.8"));
        inv.setMaxACCurrentKw(new BigDecimal("14.3"));
        inv.setContACCurrentKw(new BigDecimal("15.8"));
    }

}
