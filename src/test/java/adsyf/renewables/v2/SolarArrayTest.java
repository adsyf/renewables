package adsyf.renewables.v2;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class SolarArrayTest {

    @Test
    void getEvent() {
        MapLocation loc = new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18"));
        RoofOrientation dir = new RoofOrientation(new BigDecimal("23"),new BigDecimal("120"));
        BigDecimal cap = new BigDecimal("4");
        SolarArray seSa = new SolarArray("49 Newmarket Road",loc,dir,cap);
        log.info("built solar array, description {}",seSa.getDescription());
        ZonedDateTime startTime = ZonedDateTime.of(2025,1,1,0,0,0,0, ZoneId.of("GMT"));
        ZonedDateTime endTime = ZonedDateTime.of(2025,2,1,1,0,0,0, ZoneId.of("GMT"));
        Period period = new Period(startTime,endTime);
        period.setStartTime(startTime);
        period.setEndTime(endTime);
        EnergyEvent ee = seSa.getEvent(period);

        log.info("finished got kwhOut {}",ee.getKwhOut());
        assertTrue(ee.getKwhOut().compareTo(BigDecimal.ZERO)>0);
    }
}