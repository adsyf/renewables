package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Data
@Slf4j
public class Usage implements Comparable<Usage> {
    Period period;
    BigDecimal Kwh;
    public Usage(Period period,BigDecimal kwh){
        this.period = period;
        this.Kwh = kwh;
    }
    public Usage(String isoLocalDateTimeStart, String isoLocalDateTimeEnd, ZoneId zoneId, BigDecimal kwh){
        this(new Period(isoLocalDateTimeStart,isoLocalDateTimeEnd,zoneId),kwh);
    }
    public Usage(String isoZonedDateTimeStart, String isoZonedDateTimeEnd,BigDecimal kwh){
        this(new Period(isoZonedDateTimeStart,isoZonedDateTimeEnd),kwh);
    }
    public boolean contains(ZonedDateTime dateTime){
        return this.period.contains(dateTime);
    }
    @Override
    public String toString(){
        return this.period.toString() + ": " + this.Kwh;
    }

    public Instant getStartInstant(){
        return this.period.getStartInstant();
    }

    public long getStartEpochSecond(){
        return this.getStartInstant().getEpochSecond();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usage usage = (Usage) o;
        return Objects.equals(period, usage.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    @Override
    public int compareTo(Usage u) {
        return this.period.compareTo(u.period);
    }
}
