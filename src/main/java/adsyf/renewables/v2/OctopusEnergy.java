package adsyf.renewables.v2;

import adsyf.renewables.v2.octopus.Charges;
import adsyf.renewables.v2.octopus.OctopusService;
import adsyf.renewables.v2.octopus.OctopusServiceImpl;
import adsyf.renewables.v2.octopus.TariffParameters;
import adsyf.renewables.v2.octopus.products.HistoricalCharge;
import io.brim.renewables.v2.octopus.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

@Data
@Slf4j
public class OctopusEnergy implements EnergySupplier {

    Map<Integer, Circuit> circuits;
    Circuit meterTotals;
    Charges charges;

    //private final static int ECO7_NIGHT_START_HOUR = 0;
    //private final static int ECO7_NIGHT_END_HOUR = 7;

    /*public enum Eco7Rate {
        DAY,NIGHT,BOTH
    }*/

    /*public Eco7Rate periodEco7Span(Period period){
        ZonedDateTime periodStartNightStart = period.getStartTime().withHour(ECO7_NIGHT_START_HOUR).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime periodStartNightEnd = period.getStartTime().withHour(ECO7_NIGHT_END_HOUR).withMinute(0).withSecond(0).withNano(0);
        Period eco7NightOfPeriodStart = new Period(periodStartNightStart,periodStartNightEnd);
        Eco7Rate eco7Rate;
        if (eco7NightOfPeriodStart.getComparison(period).equals(Period.Comparison.CONTAINS)){
            eco7Rate=Eco7Rate.NIGHT;
        } else if (
                eco7NightOfPeriodStart.getComparison(period).equals(Period.Comparison.ALL_BEFORE) ||
                eco7NightOfPeriodStart.getComparison(period).equals(Period.Comparison.ALL_AFTER)
        ) {
            eco7Rate=Eco7Rate.DAY;
        } else {
            eco7Rate=Eco7Rate.BOTH;
        }
        return eco7Rate;
    }*/

    public void getHistoricCharges(TariffParameters params){
        OctopusService svc = new OctopusServiceImpl();
        this.charges = svc.getCharges(params);
    }

    public OctopusEnergy(TariffParameters params){
        getHistoricCharges(params);
    }

    public OctopusEnergy(){
    }

    public void loadHistoricCharges(Charges charges){
        this.charges = charges;
    }
    @Override
    public void putEnergyUsage(Map<Integer, Circuit> circuits) {
        this.circuits = circuits;
        for (Circuit c: circuits.values()){
            if (c.isMeterTotal){
                meterTotals = c;
            }
        }
    }

    @Override
    public EnergyEvent getUsage(Period period) {
        EnergyEvent ee = new EnergyEvent();
        ee.setType(EventType.USE);
        ee.setPeriod(period);
        Usage newUsage = new Usage(period,BigDecimal.ZERO);
        Usage usage = meterTotals.getAcKwh().ceiling(newUsage);
        if (newUsage.equals(usage)){
            ee.setKwhOut(usage.getKwh());
        } else {
            if (period.secsBetween()==usage.getPeriod().secsBetween()){
                log.warn("period {} missing, use next period {} instead if same timespan",period,usage.getPeriod());
                ee.setKwhOut(usage.getKwh());
            } else {
                throw new RuntimeException("trying to estimate usage but next period "+usage.getPeriod()+" has different span to period requested "+period);
            }
        }
        return ee;
    }

    public ArrayList<PriceBreakdown> getPriceBreakdown(TreeSet<HistoricalCharge> charges, Usage usage, PriceBreakdown.Unit unit){
        ArrayList<PriceBreakdown> priceBreakdown = new ArrayList<>();
        HistoricalCharge hc = new HistoricalCharge(usage.getPeriod());
        HistoricalCharge charge = charges.floor(hc);
        Boolean chargeAfter = Boolean.FALSE;
        while(!chargeAfter) {
            Period overlapPeriod = charge.getPeriod().getOverlapPeriod(hc.getPeriod());
            long secsOverlap = 0;
            if (overlapPeriod!=null){
                secsOverlap = overlapPeriod.secsBetween();
            }
            if (secsOverlap > 0) {
                PriceBreakdown pb = new PriceBreakdown();
                pb.setUnit(unit);
                pb.setPricePerUnit(charge.getValue_inc_vat());
                BigDecimal secsOverlapBd = new BigDecimal(secsOverlap);
                BigDecimal overAsPcnt = BigDecimal.ZERO;
                if (unit.equals(PriceBreakdown.Unit.kwh)) {
                    overAsPcnt = secsOverlapBd.divide(new BigDecimal(usage.getPeriod().secsBetween()), 5, RoundingMode.HALF_UP);
                    pb.setUnitQty(overAsPcnt.multiply(usage.getKwh()));
                } else {
                    overAsPcnt = secsOverlapBd.divide(new BigDecimal(86400), 6, RoundingMode.HALF_UP);
                    pb.setUnitQty(overAsPcnt);
                }
                pb.setPeriod(overlapPeriod);
                priceBreakdown.add(pb);
            }
            charge = charges.higher(charge);
            if (charge ==null || charge.getPeriod().getComparison(hc.getPeriod()).equals(Period.Comparison.ALL_AFTER)) {
                chargeAfter = Boolean.TRUE;
            }
        }
        return priceBreakdown;
    }

    @Override
    public Price getPriceOfElectricity(Usage usage) {
        Price price = new Price();
        ArrayList<PriceBreakdown> standingChargesPriceBreakdown = new ArrayList<>();
        //try daily charges
        if (charges.getStandingCharges().isEmpty()){
            throw new RuntimeException("standing charges should not be empty");
        } else {
            standingChargesPriceBreakdown = getPriceBreakdown(charges.getStandingCharges(),usage, PriceBreakdown.Unit.day);
        }
        ArrayList<PriceBreakdown> priceBreakdowns = new ArrayList<>(standingChargesPriceBreakdown);

        if (!charges.getStandardUnitRates().isEmpty()) {
            ArrayList<PriceBreakdown> standardUnitRatesPriceBreakdown = getPriceBreakdown(charges.getStandardUnitRates(),usage, PriceBreakdown.Unit.kwh);
            priceBreakdowns.addAll(standardUnitRatesPriceBreakdown);
        } else {
            ArrayList<PriceBreakdown> eco7UnitRatesPriceBreakdown = getPriceBreakdown(charges.getEco7UnitRates(),usage, PriceBreakdown.Unit.kwh);
        }
        price.setPriceBreakdowns(priceBreakdowns);
        return price;
    }

    @Override
    public Price getPriceOfElectricity(Period period) {
        EnergyEvent ee = getUsage(period);
        return getPriceOfElectricity(ee.getUsage());
    }
}
