package adsyf.renewables.storage;


import adsyf.renewables.components.Battery;
import adsyf.renewables.components.Inverter;
import adsyf.renewables.components.Rectifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Data
public class BatterySystem implements Battery, Rectifier, Inverter {
    private int scale = 5;
    private BigDecimal selfUseWatts;
    private final BigDecimal DEFAULT_SELF_USE_WATTS = new BigDecimal("45");
    private BigDecimal acDcEff;
    private final BigDecimal DEFAULT_AC_DC_EFF = new BigDecimal("0.96");

    private BigDecimal dcAcEff;
    private final BigDecimal DEFAULT_DC_AC_EFF = new BigDecimal("0.96");

    private BigDecimal storageEff;
    private final BigDecimal DEFAULT_STORAGE_EFF = new BigDecimal("0.96");


    private final BigDecimal C = new BigDecimal(100);
    private final MathContext V = new MathContext(5);
    private String brand;
    private String model;
    private BigDecimal socKwh = new BigDecimal(0);
    private BigDecimal capacityKwh;// = new BigDecimal(0);
    private BigDecimal maxAcDischargeKw;// = new BigDecimal(0);
    private BigDecimal maxAcChargeKw;// = new BigDecimal(0);
    private BatteryCheckedSchedules batteryCheckedSchedules;
    private ArrayList<BatteryEvent> batteryEvents = new ArrayList<>();

    public BatterySystem() {
    }

    public BatterySystem(BigDecimal capacityKwh, BigDecimal maxAcChargeKw, BigDecimal maxAcDischargeKw) {
        this.capacityKwh = capacityKwh;
        this.maxAcDischargeKw = maxAcDischargeKw;
        this.maxAcChargeKw = maxAcChargeKw;
    }

    public BigDecimal charge(ZonedDateTime chargeStarttime, BigDecimal dcEnergyKwh, BigDecimal timeHours){
        BatteryEvent be = new BatteryEvent();
        be.setStartTime(chargeStarttime);
        be.setAction(BatteryActionType.CHARGE);
        be.setSocBeforeEvent(this.socKwh);
        be.setEventDurationHours(timeHours);
        BigDecimal avgRateOfCharge = dcEnergyKwh.divide(timeHours,V);
        BigDecimal leftOverCharge = new BigDecimal(0);
        BigDecimal totalCharge = new BigDecimal(0);
        if (avgRateOfCharge.compareTo(this.maxAcChargeKw) > 1) {
            totalCharge = timeHours.multiply(this.maxAcChargeKw);
            leftOverCharge = dcEnergyKwh.subtract(totalCharge);
        } else {
            totalCharge = dcEnergyKwh;
        }
        this.socKwh = socKwh.add(totalCharge);

        if (this.socKwh.compareTo(capacityKwh) > 0){
            leftOverCharge = leftOverCharge.add(this.socKwh.subtract(capacityKwh));
            this.socKwh = this.capacityKwh;
        }
        be.setSocAfterEvent(this.socKwh);
        be.setEnergyKwh(dcEnergyKwh.subtract(leftOverCharge));
        this.batteryEvents.add(be);
        return leftOverCharge;
    }
    public BigDecimal discharge(ZonedDateTime disChargeStarttime,BigDecimal dcEnergyKwh, BigDecimal timeHours){
        BatteryEvent be = new BatteryEvent();
        be.setStartTime(disChargeStarttime);
        be.setAction(BatteryActionType.DISCHARGE);
        be.setSocBeforeEvent(this.socKwh);
        be.setEventDurationHours(timeHours);
        BigDecimal avgRateOfDischarge = dcEnergyKwh.divide(timeHours,V);
        BigDecimal leftOverCharge = new BigDecimal(0);
        BigDecimal totalCharge = new BigDecimal(0);

        if (avgRateOfDischarge.compareTo(this.maxAcDischargeKw) > 1) {
            totalCharge = timeHours.multiply(this.maxAcDischargeKw);
            leftOverCharge = dcEnergyKwh.subtract(totalCharge);
        } else {
            totalCharge = dcEnergyKwh;
        }
        this.socKwh = socKwh.subtract(totalCharge);

        if (this.socKwh.compareTo(new BigDecimal(0)) < 0){
            leftOverCharge = leftOverCharge.add(this.socKwh.abs());
            this.socKwh = BigDecimal.valueOf(0);
        }
        be.setSocAfterEvent(this.socKwh);
        be.setEnergyKwh(dcEnergyKwh.subtract(leftOverCharge));
        this.batteryEvents.add(be);
        return leftOverCharge;
    }

    @Override
    public BigDecimal getStorageEff() {
        return Objects.requireNonNullElse(this.storageEff, this.DEFAULT_STORAGE_EFF);
    }

    public void printEvents(){
        DateTimeFormatter fmt  = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss");
        String printFrmt = "%20s %10s %8s %5s %10s %10s %10s";

        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf(printFrmt,"Time","Action","Dur (h)", "KWh", "preSoc","postSoc", "postSoc%");
        System.out.println();

        for (BatteryEvent be: batteryEvents){
            System.out.format(printFrmt,be.getStartTime().format(fmt),be.getAction(),be.getEventDurationHours(), be.getEnergyKwh(), be.getSocBeforeEvent(),be.getSocAfterEvent(), be.getSocAfterEvent().divide(this.capacityKwh,V).multiply(C).setScale(2,RoundingMode.HALF_UP));
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    public static void main (String[] args){
        BatterySystem bat = new BatterySystem(new BigDecimal("13.5"),new BigDecimal(5),new BigDecimal(11));
        bat.brand = "Tesla";
        bat.model = "PowerWall 3";
        ZonedDateTime t1 = ZonedDateTime.of(2024,11,1,10,0,0,0, ZoneId.of("GMT"));
        BigDecimal lostCharge = bat.charge(t1, new BigDecimal(7), new BigDecimal(1));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t2 = ZonedDateTime.of(2024,11,1,11,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.charge(t2, new BigDecimal(7), new BigDecimal(1)));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t3 = ZonedDateTime.of(2024,11,1,12,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.charge(t3, new BigDecimal(7), new BigDecimal("0.5")));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t4 = ZonedDateTime.of(2024,11,1,13,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.discharge(t3, new BigDecimal(9), new BigDecimal("0.5")));
        log.info("total lost charge {}",lostCharge);
        bat.printEvents();
    }

    @Override
    public BigDecimal getSelfUseWatts() {
        return Objects.requireNonNullElse(this.selfUseWatts, this.DEFAULT_SELF_USE_WATTS);
    }

    @Override
    public BigDecimal getDcAcEff() {
        return Objects.requireNonNullElse(this.dcAcEff, this.DEFAULT_DC_AC_EFF);
    }

    @Override
    public BigDecimal getAcDcEff() {
        return Objects.requireNonNullElse(this.acDcEff, this.DEFAULT_AC_DC_EFF);
    }

    @Override
    public BigDecimal getDcUsedKWh(BigDecimal acRequestedKwh) {
        return acRequestedKwh.divide(this.getDcAcEff(),V);
    }

    @Override
    public BigDecimal getAcOutKWh(BigDecimal dcInKwh) {
        return dcInKwh.multiply(this.getDcAcEff(),V);
    }

    @Override
    public BigDecimal getInvMaxDcKw() {
        return this.getInvMaxAcKw().multiply(this.acDcEff,V);
    }

    @Override
    public BigDecimal getInvMaxAcKw() {
        if (this.maxAcChargeKw==null){
            throw new RuntimeException("battery system is missing maxAcChargeKw");
        } else {
            return this.maxAcChargeKw;
        }
    }


    //ac-to-dc
    @Override
    public BigDecimal getAcUsedKWh(BigDecimal dcRequestedKwh) {
        return dcRequestedKwh.divide(this.getAcDcEff(),V);
    }

    @Override
    public BigDecimal getDcOutKWh(BigDecimal acInKwh) {
        return acInKwh.multiply(this.getAcDcEff(),V);
    }

    @Override
    public BigDecimal getRecMaxDcKw() {
        return this.getRecMaxAcKw().divide(this.getDcAcEff(),V);
    }

    @Override
    public BigDecimal getRecMaxAcKw() {
        if (this.maxAcDischargeKw==null){
            throw new RuntimeException("battery system is missing maxAcDischargeKw");
        } else {
            return this.maxAcDischargeKw;
        }
    }

    @Override
    public BigDecimal gridCharge(ZonedDateTime chargeStarttime, BigDecimal acEnergyKwh, BigDecimal timeHours) {
        BigDecimal dcEnergyKwh = acEnergyKwh.multiply(this.getDcAcEff());
        return this.charge(chargeStarttime,dcEnergyKwh,timeHours);
    }

    @Override
    public BigDecimal gridDischarge(ZonedDateTime disChargeStarttime, BigDecimal acEnergyKwh, BigDecimal timeHours) {
        BigDecimal dcEnergyKwh = acEnergyKwh.multiply(this.getDcAcEff());
        return this.discharge(disChargeStarttime,dcEnergyKwh,timeHours);
    }

}
