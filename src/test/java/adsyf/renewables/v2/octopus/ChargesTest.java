package adsyf.renewables.v2.octopus;

import adsyf.renewables.v2.octopus.products.HistoricalCharge;

import java.time.ZoneId;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class ChargesTest {

    //@Test
    void setEco7UnitRates1() {
        ZoneId ldn = ZoneId.of("Europe/London");
        TreeSet<HistoricalCharge> dayUnitRates = new TreeSet<>();
        TreeSet<HistoricalCharge> nightUnitRates = new TreeSet<>();
        dayUnitRates.add(new HistoricalCharge("2025-01-01T00:00", "2026-01-01T00:00", ldn, "ALL","0.50"));
        nightUnitRates.add(new HistoricalCharge("2025-01-01T00:00", "2026-01-01T00:00", ldn, "ALL","0.25"));
        Charges charges = new Charges(null,null,dayUnitRates,nightUnitRates);
        charges.setEco7UnitRates();
        assertEquals(736, charges.getEco7UnitRates().size());
    }
    //@Test
    void setEco7UnitRates2() {
        ZoneId ldn = ZoneId.of("Europe/London");
        TreeSet<HistoricalCharge> dayUnitRates = new TreeSet<>();
        TreeSet<HistoricalCharge> nightUnitRates = new TreeSet<>();
        dayUnitRates.add(new HistoricalCharge("2024-01-01T05:00", "2024-04-01T13:00", ldn, "ALL","0.50"));
        dayUnitRates.add(new HistoricalCharge("2024-04-01T13:00", "2024-04-01T14:00", ldn, "ALL","0.60"));
        dayUnitRates.add(new HistoricalCharge("2024-04-01T14:00", "2024-04-01T15:00", ldn, "ALL","0.70"));
        dayUnitRates.add(new HistoricalCharge("2024-04-01T15:00", "2024-04-01T16:00", ldn, "ALL","0.80"));
        dayUnitRates.add(new HistoricalCharge("2024-04-01T16:00", "2024-08-01T00:00", ldn, "ALL","0.90"));
        dayUnitRates.add(new HistoricalCharge("2024-08-01T00:00", "2025-01-01T00:00", ldn, "ALL","0.100"));
        nightUnitRates.add(new HistoricalCharge("2024-01-01T05:00", "2024-04-01T13:00", ldn, "ALL","0.25"));
        nightUnitRates.add(new HistoricalCharge("2024-04-01T13:00", "2024-08-01T00:00", ldn, "ALL","0.30"));
        nightUnitRates.add(new HistoricalCharge("2024-08-01T00:00", "2025-01-01T00:00", ldn, "ALL","0.35"));
        Charges charges = new Charges(null,null,dayUnitRates,nightUnitRates);
        charges.setEco7UnitRates();
        assertEquals(704, charges.getEco7UnitRates().size());
    }
}