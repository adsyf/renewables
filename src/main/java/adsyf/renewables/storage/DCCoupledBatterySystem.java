package adsyf.renewables.storage;

import adsyf.renewables.components.HybridInverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


@Data
@Slf4j
public class DCCoupledBatterySystem extends BatterySystem implements HybridInverter {
    private final BigDecimal DEFAULT_DC_DC_EFF = new BigDecimal("0.98");
    private BigDecimal dcDcEff;
    private BigDecimal maxDcInKw;
    private BigDecimal maxDcOutKw;
    private ArrayList<DcDcConverterEvent> dcDcConverterEvents = new ArrayList<>();
    private BigDecimal inTtlKwh = BigDecimal.ZERO;
    private BigDecimal batTtlKwh = BigDecimal.ZERO;
    private BigDecimal drawTtlKwh = BigDecimal.ZERO;
    private BigDecimal gridTtlKwh = BigDecimal.ZERO;
    private BigDecimal effLossTtlKwh = BigDecimal.ZERO;
    private BigDecimal clipLossTtlKwh = BigDecimal.ZERO;


    @Override
    public BigDecimal getDcDcEff() {
        return Objects.requireNonNullElse(this.dcDcEff, this.DEFAULT_DC_DC_EFF);
    }

    @Override
    public BigDecimal getMaxDcInKw() {
        if (this.maxDcInKw!=null){
            return this.maxDcInKw;
        } else {
            throw new RuntimeException("maxDcInKw is null");
        }
    }

    @Override
    public BigDecimal getMaxDcOutKw() {
        if (this.maxDcOutKw!=null){
            return this.maxDcOutKw;
        } else {
            throw new RuntimeException("maxDcOutKw is null");
        }
    }

    @Override
    public ArrayList<DcDcConverterEvent> getDcDcConverterEvents(){
        return this.dcDcConverterEvents;
    };

    public void operate(ZonedDateTime startTime, BigDecimal solarEnergyInDcKwh, BigDecimal timeHours, BigDecimal drawAcKwh) {
        DcDcConverterEvent e = new DcDcConverterEvent();
        e.setStartTime(startTime);
        e.setEventDurationHours(timeHours);
        BigDecimal avgSolarInDcKw = solarEnergyInDcKwh.divide(timeHours, this.getV());
        BigDecimal avgDrawAcKw = drawAcKwh.divide(timeHours, this.getV());
        if (avgSolarInDcKw.compareTo(this.getMaxDcInKw())>0){
            e.setErrorInKw(avgSolarInDcKw);
            e.setErrorMaxInKw(this.getMaxDcInKw());
            this.getDcDcConverterEvents().add(e);
            log.error("Avg solar in {} exceeded DCDCConverter Max {}",avgSolarInDcKw,this.getMaxDcInKw());
        } else {
            e.setInKwh(solarEnergyInDcKwh);
            BigDecimal maxConvertedKwh = solarEnergyInDcKwh.multiply(this.getDcDcEff());
            e.setEffLossKwh(solarEnergyInDcKwh.subtract(maxConvertedKwh));
            BigDecimal convertedKwh = BigDecimal.ZERO;
            if (maxConvertedKwh.compareTo(this.getMaxDcOutKw())>0){
                convertedKwh = this.getMaxDcOutKw();

            } else {
                convertedKwh = maxConvertedKwh;
            }
            e.setClipLossKwh(maxConvertedKwh.subtract(convertedKwh));




            this.inTtlKwh = this.inTtlKwh.add(e.getInKwh());
            e.setInTtlKwh(this.inTtlKwh);
            this.batTtlKwh = this.batTtlKwh.add(e.getBatKwh());
            e.setBatTtlKwh(this.batTtlKwh);
            this.drawTtlKwh = this.drawTtlKwh.add(e.getBatKwh());
            e.setDrawTtlKwh(this.drawTtlKwh);
            this.gridTtlKwh = this.gridTtlKwh.add(e.getGridKwh());
            e.setGridTtlKwh(this.gridTtlKwh);
            this.effLossTtlKwh = this.effLossTtlKwh.add(e.getEffLossKwh());
            e.setEffLossTtlKwh(this.effLossTtlKwh);
            this.clipLossTtlKwh = this.clipLossTtlKwh.add(e.getClipLossKwh());
            e.setClipLossTtlKwh(this.clipLossTtlKwh);

            BigDecimal maxDcFromDcDcConverter = solarEnergyInDcKwh.multiply(this.getDcDcEff());


            //if (drawAcKwh)

            //BigDecimal dcEnergyKwh = solarEnergyKwh.multiply(this.getDcDcEff());
            //return this.charge(chargeStarttime,dcEnergyKwh,timeHours);

        }
    }

    public void printDcDcConverterEvents(){
        DateTimeFormatter fmt  = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss");
        String printFrmt =          "%20s  %8s        %8s   %10s    %10s      %10s     %10s        %10s       %10s     %10s     %10s      %10s      %15s         %15s";

        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf(printFrmt,"Time","Dur (h)", "In", "ToBat","ToDraw", "ToGrid","Loss(e)" , "Loss(c)", "InTtl", "BatTtl","DrawTtl","GridTtl","Loss(e)Ttl","Loss(c)Ttl");
        System.out.println();

        for (DcDcConverterEvent e: this.dcDcConverterEvents){
            System.out.format(printFrmt,e.getStartTime().format(fmt),e.getEventDurationHours(),e.inKwh,e.batKwh,e.drawKwh,e.gridKwh,e.effLossKwh,e.clipLossKwh,e.inTtlKwh,e.batTtlKwh,e.drawTtlKwh,e.gridTtlKwh,e.effLossTtlKwh,e.clipLossTtlKwh);
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------");
    }
}
