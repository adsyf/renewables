package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

@Data
@Slf4j
public class Period implements Comparable<Period> {
    private static BigDecimal secsInHour = new BigDecimal(3600);
    private final MathContext V = new MathContext(5);
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

   public Period (String isoZonedDateTimeStart, String isoZonedDateTimeEnd){
       this(ZonedDateTime.parse(isoZonedDateTimeStart),ZonedDateTime.parse(isoZonedDateTimeEnd));
   }

    public Period (String isoLocalDateTimeStart, String isoLocalDateTimeEnd, String zoneId){
        this(isoLocalDateTimeStart,isoLocalDateTimeEnd,ZoneId.of(zoneId));
    }
    public Period (String isoLocalDateTimeStart, String isoLocalDateTimeEnd, ZoneId zoneId){
        this(LocalDateTime.parse(isoLocalDateTimeStart).atZone(zoneId),LocalDateTime.parse(isoLocalDateTimeEnd).atZone(zoneId));
    }
    public Period(ZonedDateTime startTime,ZonedDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        if (this.startTime.isAfter(this.endTime) || this.startTime.equals(this.endTime)){
            throw new RuntimeException("endTime "+ this.endTime +" is not after startTime "+ this.startTime);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(startTime, period.startTime) && Objects.equals(endTime, period.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    public Instant getStartInstant(){
        return this.startTime.toInstant();
    }

    public long getStartEpochSecond(){
        return this.getStartInstant().getEpochSecond();
    }

    public long daysBetween(){
        return  ChronoUnit.DAYS.between(this.getStartTime(),this.getEndTime());
    }

    public long hoursBetween(){
        return  ChronoUnit.HOURS.between(this.getStartTime(),this.getEndTime());
    }

    public long minsBetween(){
        return  ChronoUnit.MINUTES.between(this.getStartTime(),this.getEndTime());
    }
    public long secsBetween(){
        return  ChronoUnit.SECONDS.between(this.getStartTime(),this.getEndTime());
    }

    public int hourOfYear(ZonedDateTime time){
        int hoursOfDay = (time.getDayOfYear() - 1) * 24;
        return hoursOfDay + time.getHour();
    }

    public int hourOfYearIgnoringLeap(ZonedDateTime time){
        ZonedDateTime adjustedTime;
        if (Year.of(time.getYear()).isLeap()){
            adjustedTime = time.plusYears(1);
        } else {
            adjustedTime = time;
        }
        int hoursOfDay = (adjustedTime.getDayOfYear() - 1) * 24;
        return hoursOfDay + adjustedTime.getHour();
    }

    public int startHourOfYear(){
        return hourOfYear(this.startTime);
    }
    public int startHourOfYearIgnoringLeap(){
        return hourOfYearIgnoringLeap(this.startTime);
    }

    public BigDecimal percentOfHoursBetween(ZonedDateTime from,ZonedDateTime to){
        BigDecimal secsBetween = new BigDecimal(ChronoUnit.SECONDS.between(from,to));
        return secsBetween.divide(secsInHour,V);
    }

    private ArrayList<YearHour> shiftForLeapYear(ArrayList<YearHour> yearHours){
        boolean needsAdjusting = false;
        for (YearHour yh:yearHours){
            if (yh.getHourOfYear()>8670){
                needsAdjusting=true;
                break;
            }
        }
        if (needsAdjusting){
            ArrayList<YearHour> newYearHours = new ArrayList<>();
            boolean adjustFutureTimes = false;
            for (YearHour yh:yearHours){

                if (yh.getHourOfYear()>8670){
                    adjustFutureTimes = true;
                }
                int newHour =  yh.getHourOfYear();
                if (adjustFutureTimes) {
                    if (newHour>8670){
                        newHour = newHour%8760;
                    } else {
                        newHour = newHour + 24;
                    }
                }
                newYearHours.add(new YearHour(newHour,yh.getPercentOfHour()));
            }
            return newYearHours;
        } else {
            return yearHours;
        }
    }

    public ArrayList<YearHour> yearHoursBetween(){
        ArrayList<YearHour> yearHours = new ArrayList<>();
        if (secsBetween()> 31536000){
            throw new RuntimeException("cannot return value if period > 1 non leap year");
        }
        if ((hourOfYear(this.startTime) == hourOfYear(this.endTime)) && (this.startTime.getYear() == this.endTime.getYear())){
            log.debug("same year same hour adding time between period");
            yearHours.add(new YearHour(hourOfYear(this.startTime),percentOfHoursBetween(this.startTime,this.endTime)));
        } else {

            ZonedDateTime endOfStartHour = ZonedDateTime.of(this.startTime.getYear(),this.startTime.getMonthValue(),this.startTime.getDayOfMonth(),this.startTime.getHour() + 1,0,0,0, this.startTime.getZone());
            log.debug("adding percentage of start hour");
            yearHours.add(new YearHour(hourOfYear(this.startTime),percentOfHoursBetween(this.startTime,endOfStartHour)));
            ZonedDateTime startHour = endOfStartHour;
            ZonedDateTime startOfEndHour = ZonedDateTime.of(this.endTime.getYear(),this.endTime.getMonthValue(),this.endTime.getDayOfMonth(),this.endTime.getHour(),0,0,0, this.endTime.getZone());
            while (startHour.isBefore(startOfEndHour)){
                log.debug("adding whole hours this hour {}",startHour);
                yearHours.add(new YearHour(hourOfYear(startHour),new BigDecimal(1)));
                startHour = startHour.plusHours(1);
            }
            log.debug("adding percentage of end hour");
            yearHours.add(new YearHour(hourOfYear(this.endTime),percentOfHoursBetween(startOfEndHour,this.endTime)));

        }
        log.debug("initial hours");
        for (YearHour yh:yearHours){
            log.trace(yh.toString());
        }
        return shiftForLeapYear(yearHours);
    }

    public enum Comparison {
        /*
               period       |         |
         compare period
                            |         |       EQUAL
                                | |           CONTAINS
                          |   |               PART_AFTER
                                     |  |     PART_BEFORE
                        | |                   ALL_AFTER
                                         | |  ALL_BEFORE
                        |                   | CONTAINED
         */

        CONTAINS, PART_AFTER, PART_BEFORE, ALL_AFTER, ALL_BEFORE, CONTAINED, EQUAL
    }

    public boolean contains(ZonedDateTime dateTime){
        return this.startTime.compareTo(dateTime) <= 0 && this.endTime.compareTo(dateTime) >= 0;
    }

    public Boolean isOverlap(Period period){
        return this.contains(period.startTime) || this.contains(period.endTime) || period.contains(this.startTime) || period.contains(this.endTime);
    }

    public long getSecsOverlap(Period period){
        long overlap = 0;
        if (isOverlap(period)) {
            Period overlapPeriod = getOverlapPeriod(period);
            overlap = overlapPeriod.secsBetween();
        }
        return overlap;
    }



    public Period getOverlapPeriod(Period period){
        if (isOverlap(period)) {
            ZonedDateTime maxStart = this.startTime.isAfter(period.startTime) ? this.startTime : period.startTime;
            ZonedDateTime minEnd = this.endTime.isBefore(period.endTime) ? this.endTime : period.endTime;
            return new Period(maxStart,minEnd);
        } else {
            return null;
        }

    }

    public Comparison getComparison(Period period){
        Comparison comp;
        if (this.startTime.equals(period.startTime) && this.endTime.isEqual(period.endTime)) {
            comp=Comparison.EQUAL;
        } else if (
            (this.startTime.isBefore(period.startTime) || this.startTime.isEqual(period.startTime)) &&
            (this.endTime.isAfter(period.endTime) || this.endTime.isEqual(period.endTime))
            ) {
            comp=Comparison.CONTAINS;
        } else if (
                (this.startTime.isAfter(period.endTime) || this.startTime.equals(period.endTime))
        ) {
            comp=Comparison.ALL_AFTER;
        } else if (
                (this.endTime.isBefore(period.startTime) || this.endTime.equals(period.startTime))
        ) {
            comp=Comparison.ALL_BEFORE;
        } else if (
            (this.startTime.isEqual(period.startTime) || this.startTime.isAfter(period.startTime)) &&
            (this.endTime.isAfter(period.endTime))
                   ) {
            comp=Comparison.PART_AFTER;
        } else if (
                (this.startTime.isBefore(period.startTime)) &&
                (this.endTime.isBefore(period.endTime) || this.endTime.isEqual(period.endTime))
        ) {
            comp=Comparison.PART_BEFORE;
        } else if (
                (this.startTime.isAfter(period.startTime) || this.startTime.isEqual(period.startTime)) &&
                (this.endTime.isBefore(period.endTime) || this.endTime.isEqual(period.endTime))
        ) {
            comp=Comparison.CONTAINED;
        } else {
            throw new RuntimeException("unknown comparison found");
        }
        return comp;
    }


    public static void main(String[] args){
        ZonedDateTime start = ZonedDateTime.of(2024,12,29,22,30,0,0, ZoneId.of("GMT"));
        ZonedDateTime end = ZonedDateTime.of(2025,1,1,3,30,0,0, ZoneId.of("GMT"));
        Period period = new Period(start,end);
        ArrayList<YearHour> yearHours = period.yearHoursBetween();
        log.info("adjusted hours");
        for (YearHour yh:yearHours){
            log.info(yh.toString());
        }
    }

    @Override
    public int compareTo(Period p) {
        if (this.startTime.compareTo(p.startTime) != 0){
            return this.startTime.compareTo(p.startTime);
        } else {
            return  this.endTime.compareTo(p.endTime);
        }
    }
}
