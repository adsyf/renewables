package adsyf.renewables.v2;

import adsyf.renewables.v2.metering.providers.emporia.CsvRawDataImport;
import adsyf.renewables.v2.octopus.Charges;
import adsyf.renewables.v2.octopus.PaymentMethod;
import adsyf.renewables.v2.octopus.TariffParameters;
import adsyf.renewables.v2.octopus.products.HistoricalCharge;
import com.opencsv.exceptions.CsvException;
import adsyf.renewables.v2.octopus.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class OctopusEnergyTest {


    @Test
    void getUsage() throws IOException, CsvException {
        OctopusEnergy oe = new OctopusEnergy(new TariffParameters("VAR-22-11-01", PaymentType.directDebitMonthly,"RH106ND", PaymentMethod.DIRECT_DEBIT));
        Map<Integer, Circuit> circuits = new HashMap<>();
        File file = new File("C:\\Users\\adsyf\\Downloads\\data-export-208020-8380958110011221080\\58A778-Newmarket_Road-1H.csv");
        CsvRawDataImport importer = new CsvRawDataImport();
        importer.populateCircuitsWithHourlyUsage(file,circuits);
        oe.putEnergyUsage(circuits);
        ZonedDateTime startTime = ZonedDateTime.of(2024,1,13,20,00,0,0, ZoneId.of("GMT"));
        ZonedDateTime endTime = startTime.plusHours(1);
        Period period = new Period(startTime, endTime);
        EnergyEvent ee = oe.getUsage(period);
        assertEquals(ee.getKwhOut().setScale(4),new BigDecimal("1.2978"));
        log.info("got it");
    }

    @Test
    void getEnergyPrices() {
    }

    @Test
    void loadHistoricCharges() {
    }
    @Test
    void testIsoFormats(){
        LocalDateTime ldt = LocalDateTime.parse("2024-01-01T00:00");
        ZoneId london = ZoneId.of("Europe/London");
        ZonedDateTime z1 = ldt.atZone(london);
    }

    @Test
    void getPriceOfElectricity1() {
        ZoneId ldn = ZoneId.of("Europe/London");
        TreeSet<HistoricalCharge> standingCharges = new TreeSet<>();
        standingCharges.add(new HistoricalCharge("2024-01-01T00:00", "2024-07-01T00:00", ldn, "ALL","0.24"));
        standingCharges.add(new HistoricalCharge("2024-07-01T00:00", "2024-12-31T00:00", ldn, "ALL", "0.48"));

        TreeSet<HistoricalCharge> standardUnitRates = new TreeSet<>();
        standardUnitRates.add(new HistoricalCharge("2024-01-01T00:00", "2024-01-01T01:00", ldn, "ALL", "0.50"));
        standardUnitRates.add(new HistoricalCharge("2024-01-01T01:00", "2024-01-01T02:00", ldn, "ALL", "0.60"));
        standardUnitRates.add(new HistoricalCharge("2024-01-01T02:00", "2024-01-01T03:00", ldn, "ALL", "0.70"));
        standardUnitRates.add(new HistoricalCharge("2024-01-01T03:00", "2024-01-01T04:00", ldn, "ALL", "0.80"));

        TreeSet<HistoricalCharge> dayUnitRates = new TreeSet<>();
        TreeSet<HistoricalCharge> nightUnitRates = new TreeSet<>();
        Charges charges = new Charges(standingCharges,standardUnitRates,dayUnitRates,nightUnitRates);

        OctopusEnergy oe = new OctopusEnergy();
        oe.loadHistoricCharges(charges);

        Usage u1 = new Usage("2024-01-01T01:30","2024-01-01T02:30",ldn,new BigDecimal("2"));
        Price price = oe.getPriceOfElectricity(u1);
        BigDecimal tp = price.getTotalPrice();
        assertEquals(price.getTotalPrice(),new BigDecimal("1.31000008"));
    }

    @Test
    void getPriceOfElectricity2() {
        ZoneId ldn = ZoneId.of("Europe/London");
        TreeSet<HistoricalCharge> standingCharges = new TreeSet<>();
        standingCharges.add(new HistoricalCharge("2024-01-01T00:00", "2024-12-31T00:00", ldn, "ALL","0.55"));

        TreeSet<HistoricalCharge> standardUnitRates = new TreeSet<>();
        standardUnitRates.add(new HistoricalCharge("2024-01-01T00:00", "2025-01-01T01:00", ldn, "ALL", "0.50"));

        TreeSet<HistoricalCharge> dayUnitRates = new TreeSet<>();
        TreeSet<HistoricalCharge> nightUnitRates = new TreeSet<>();
        Charges charges = new Charges(standingCharges,standardUnitRates,dayUnitRates,nightUnitRates);

        OctopusEnergy oe = new OctopusEnergy();
        oe.loadHistoricCharges(charges);

        Usage u1 = new Usage("2024-01-02T00:00","2024-01-03T00:00",ldn,new BigDecimal("40"));
        Price price = oe.getPriceOfElectricity(u1);
        BigDecimal tp = price.getTotalPrice().setScale(2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("20.55"),tp);
    }

    @Test
    void getPriceOfElectricity3() {
        OctopusEnergy oe = new OctopusEnergy();
        oe.loadHistoricCharges(charges);
        Price price = getPriceOfElectricity(period);
    }
}