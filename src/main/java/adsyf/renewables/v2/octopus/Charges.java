package adsyf.renewables.v2.octopus;

import adsyf.renewables.v2.octopus.products.HistoricalCharge;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

@Data
@Slf4j
public class Charges {
    final static Integer eco7DayStart =  7;
    final static Integer eco7DayEnd =  24;
    TreeSet<HistoricalCharge> standingCharges;
    TreeSet<HistoricalCharge> standardUnitRates;
    TreeSet<HistoricalCharge> dayUnitRates;
    TreeSet<HistoricalCharge> nightUnitRates;
    TreeSet<HistoricalCharge> eco7UnitRates;

    private void addRates(TreeSet<HistoricalCharge> rates, TreeSet<HistoricalCharge> combinedRates, HistoricalCharge hc){
        HistoricalCharge rate = rates.floor(hc);
        if (rate==null){
            rate = rates.first();
        }
        if (!rate.getValidFrom().isAfter(hc.getValidFrom())){
            ZonedDateTime validFrom = hc.getValidFrom();
            //int cnt =0;
            while (rate!=null && !rate.getValidTo().isAfter(hc.getValidTo())){
                //cnt++;
                //if (cnt>=2000){
                //    log.error("why so high?");
                //}
                HistoricalCharge nightChargeX = new HistoricalCharge();
                nightChargeX.setValidFrom(validFrom);
                nightChargeX.setValidTo(rate.getValidTo());
                nightChargeX.setValue_inc_vat(rate.getValue_inc_vat());
                nightChargeX.setPaymentMethod(rate.getPaymentMethod());
                if(nightChargeX.getValidFrom().equals(nightChargeX.getValidTo())){
                    log.error("rate with zero time period {}",nightChargeX.getValidFrom());
                } else {
                    combinedRates.add(nightChargeX);
                }
                validFrom = rate.getValidTo();
                rate = rates.higher(rate);
            }
            if (rate != null){
                hc.setValidFrom(validFrom);
                hc.setValue_inc_vat(rate.getValue_inc_vat());
                hc.setPaymentMethod(rate.getPaymentMethod());
                if(hc.getValidFrom().equals(hc.getValidTo())){
                    log.error("rate with zero time period {}",hc.getValidFrom());
                } else {
                    combinedRates.add(hc);
                }
            }

        } else {
            throw new RuntimeException("earliest rate start "+ rate.getValidFrom() + " but charge start " + hc.getValidFrom());
        }
    }
    public void setEco7UnitRates(){
        TreeSet<HistoricalCharge> combinedRates = new TreeSet<>();
        HistoricalCharge firstDayUnitRate = dayUnitRates.first();
        HistoricalCharge firstNightUnitRate = nightUnitRates.first();
        HistoricalCharge lastDayUnitRate = dayUnitRates.last();
        HistoricalCharge lastNightUnitRate = nightUnitRates.last();
        ZonedDateTime firstTime = firstDayUnitRate.getValidFrom().isBefore(firstNightUnitRate.getValidFrom()) ? firstDayUnitRate.getValidFrom() : firstNightUnitRate.getValidFrom();
        ZonedDateTime lastTime = lastDayUnitRate.getValidTo().isAfter(lastNightUnitRate.getValidTo()) ? lastDayUnitRate.getValidTo() : lastNightUnitRate.getValidTo();
        ZonedDateTime nextPeriodStart = firstTime;

        while (nextPeriodStart.isBefore(lastTime)){
            //if (combinedRates.size()>=429){
             //   log.info("here");
            //}
            //ZonedDateTime eco7DayStartDt = nextPeriodStart.truncatedTo(ChronoUnit.DAYS).plusHours(eco7DayStart);
            ZonedDateTime eco7DayStartDt = nextPeriodStart.truncatedTo(ChronoUnit.DAYS).withHour(eco7DayStart);
            ZonedDateTime eco7DayEndDt = nextPeriodStart.truncatedTo(ChronoUnit.DAYS).plusDays(1);

            if (nextPeriodStart.isBefore(eco7DayStartDt)){
                HistoricalCharge nightCharge = new HistoricalCharge();
                nightCharge.setValidFrom(nextPeriodStart);
                if (eco7DayStartDt.isBefore(lastTime)){
                    nightCharge.setValidTo(eco7DayStartDt);
                } else {
                    nightCharge.setValidTo(lastTime);
                }
                addRates(nightUnitRates, combinedRates, nightCharge);

                nextPeriodStart = eco7DayStartDt;
            }

            if (nextPeriodStart.isBefore(lastTime)){
                HistoricalCharge dayCharge = new HistoricalCharge();
                dayCharge.setValidFrom(nextPeriodStart);
                if (eco7DayEndDt.isBefore(lastTime)){
                    dayCharge.setValidTo(eco7DayEndDt);
                } else {
                    dayCharge.setValidTo(lastTime);
                }
                addRates(dayUnitRates, combinedRates, dayCharge);
                nextPeriodStart = eco7DayEndDt;
            }
        }
        this.eco7UnitRates = combinedRates;

    }
    public Charges(TreeSet<HistoricalCharge> standingCharges,TreeSet<HistoricalCharge> standardUnitRates, TreeSet<HistoricalCharge> dayUnitRates, TreeSet<HistoricalCharge> nightUnitRates){
        this.standingCharges = standingCharges;
        this.standardUnitRates = standardUnitRates;
        this.dayUnitRates = dayUnitRates;
        this.nightUnitRates = nightUnitRates;
        if (!(this.dayUnitRates.isEmpty() && this.nightUnitRates.isEmpty())){
            this.setEco7UnitRates();
        }
    }
}
