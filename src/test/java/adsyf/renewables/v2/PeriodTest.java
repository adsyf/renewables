package adsyf.renewables.v2;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {
    private final static ZonedDateTime P_START = ZonedDateTime.of(2024,1,1,10, 0,0,0, ZoneId.of("GMT"));
    private final static ZonedDateTime P_END = ZonedDateTime.of(2024,1,1,11, 0,0,0, ZoneId.of("GMT"));
    private final static Period P = new Period(P_START,P_END);
    @Test
    void getComparisonContains() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,10, 15,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,10, 45,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.CONTAINS,P.getComparison(compP));
    }
    @Test
    void getComparisonPartAfter() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,9, 15,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,10, 45,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.PART_AFTER,P.getComparison(compP));
    }
    @Test
    void getComparisonPartBefore() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,10, 30,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,11, 30,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.PART_BEFORE,P.getComparison(compP));
    }
    @Test
    void getComparisonAllAfter() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,9, 30,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,10, 0,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);

        assertEquals(Period.Comparison.ALL_AFTER,P.getComparison(compP));
    }
    @Test
    void getComparisonAllBefore() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,11, 30,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,12, 0,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.ALL_BEFORE,P.getComparison(compP));
    }
    @Test
    void getComparisonContained() {
        ZonedDateTime s = ZonedDateTime.of(2024,1,1,9, 30,0,0, ZoneId.of("GMT"));
        ZonedDateTime e = ZonedDateTime.of(2024,1,1,11, 0,0,0, ZoneId.of("GMT"));
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.CONTAINED,P.getComparison(compP));
    }
    @Test
    void getComparisonEqual() {
        ZonedDateTime s = P_START;
        ZonedDateTime e = P_END;
        Period compP = new Period(s,e);
        assertEquals(Period.Comparison.EQUAL,P.getComparison(compP));
    }


}