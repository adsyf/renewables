package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Slf4j
public class SolarInstallation {
    ArrayList<SolarArray> solarArrays;
    public ArrayList<EnergyEvent> getEvents(Period period){
        ArrayList<EnergyEvent> events = new ArrayList<>();
        EnergyEvent totalEnergyEvent = new EnergyEvent();
        totalEnergyEvent.setType(EventType.SOLAR_TOTAL);
        BigDecimal totalKwhOut = BigDecimal.ZERO;
        for (SolarArray sa:solarArrays){
            EnergyEvent ee = sa.getEvent(period);
            events.add(ee);
            totalKwhOut = totalKwhOut.add(ee.getKwhOut());
        }
        totalEnergyEvent.setKwhOut(totalKwhOut);
        events.add(totalEnergyEvent);
        return events;
    }
}
