package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Data
public class TimeBlock implements Comparable<TimeBlock> {
    final static DateTimeFormatter hFmt = DateTimeFormatter.ofPattern("MMddHH");
    final static DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("MMdd");
    final static DateTimeFormatter mFmt = DateTimeFormatter.ofPattern("MM");
    public final static int HOURS_IN_NORMAL_YEAR=8760;
    public final static int HOURS_IN_LEAP_YEAR=8784;
    ZonedDateTime startTime;
    ChronoUnit unit;
    public Integer getBlockOfYear(ChronoUnit unit){

        //log.debug("startTime: {}, startYear: {}, hours: {}",this.startTime.format(fmt), startYear.format(fmt),startYear.until(this.startTime, ChronoUnit.HOURS));

        String ret = switch (unit) {
            case HOURS -> this.startTime.format(hFmt);
            case DAYS -> this.startTime.format(dFmt);
            case MONTHS -> this.startTime.format(mFmt);
            default -> "";
        };
        return Integer.valueOf(ret);
    }

    public Integer getUnitOfYear(){
        return this.getBlockOfYear(this.getUnit());
    }

    public TimeBlock (ZonedDateTime startTime,ChronoUnit unit) {
        this.startTime = startTime;
        this.unit = unit;
    }
    @Override
    public int compareTo(TimeBlock other){
        return Long.compare(this.startTime.toEpochSecond(), other.startTime.toEpochSecond());
    }

    public static void main(String[] args){
        ZonedDateTime time1 = ZonedDateTime.now(ZoneId.of("GMT"));
        ZonedDateTime time2 = ZonedDateTime.of(2024,1,1,23,59,0,0,ZoneId.of("GMT"));
        TimeBlock tb = new TimeBlock(time2,ChronoUnit.HOURS);
        log.info("Hour of year is {}", tb.getBlockOfYear(ChronoUnit.HOURS));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeBlock that = (TimeBlock) o;
        return startTime.equals(that.startTime) &&
                unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, unit);
    }
}
