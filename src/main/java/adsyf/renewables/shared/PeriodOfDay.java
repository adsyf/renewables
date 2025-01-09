package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
public class PeriodOfDay {
    int startHour;
    int startMin;
    int startNextHour;
    int startNextMin;
    Boolean startHourSet = Boolean.FALSE;
    Boolean startNextHourSet = Boolean.FALSE;
    Boolean startMinSet = Boolean.FALSE;
    Boolean startNextMinSet = Boolean.FALSE;
    public enum FieldType {
        START, NEXT
    }

    private void checkHour(int hourOfDay,FieldType type){
        if (startHour < 0){
            throw new UnsupportedOperationException(type + " hour must be > 0");
        }
        if (startHour > 23){
            throw new UnsupportedOperationException(type + " hour must be <= 23");
        }
    }
    private void checkMin(int minOfHour,FieldType type){
        if (minOfHour < 0){
            throw new UnsupportedOperationException(type + " minute must be > 0");
        }
        if (minOfHour > 59){
            throw new UnsupportedOperationException(type + " minute must be <= 60");
        }
    }
    public void setStartHour(int startHour) {
        checkHour(startHour,FieldType.START);
        this.startHour = startHour;
        this.startHourSet = Boolean.TRUE;
    }
    public void setStartNextHour(int startNextHour) {
        checkHour(startHour,FieldType.NEXT);
        this.startNextHour = startNextHour;
        this.startNextHourSet = Boolean.TRUE;
    }
    public void setStartMin(int startMin){
        checkMin(startMin,FieldType.START);
        this.startMin = startMin;
        this.startMinSet = Boolean.TRUE;
    }
    public void setStartNextMin(int startNextMin){
        checkMin(startNextMin,FieldType.NEXT);
        this.startNextMin = startNextMin;
        this.startNextMinSet = Boolean.TRUE;
    }
    private void checkFieldsSet(){
        if (!(startHourSet && startNextHourSet && startMinSet && startNextMinSet)){
            throw new RuntimeException("all fields must be set");
        }
    }
    public PeriodOfDay(){}
    public PeriodOfDay(int startHour, int startMin, int startNextHour, int startNextMin){
        this.setStartHour(startHour);
        this.setStartMin(startMin);
        this.setStartNextHour(startNextHour);
        this.setStartNextMin(startNextMin);
    }
    public static DateRange getArbitraryDateRange(PeriodOfDay pod){
        LocalDateTime dt = LocalDateTime.of(2001,1,1,0,0);
        int thisStartMinOfDay = (pod.startHour * 60) + pod.startMin;
        int thisNextMinOfDay = (pod.startNextHour * 60) + pod.startNextMin;
        LocalDateTime thisStartDt = dt.plusMinutes(thisStartMinOfDay);
        LocalDateTime thisNextDt;
        if (thisNextMinOfDay > thisStartMinOfDay){
            thisNextDt = dt.plusMinutes(thisStartMinOfDay);
        } else {
            //crosses midnight
            thisNextDt = dt.plusDays(1).plusMinutes(thisNextMinOfDay);
        }
        DateRange dr = new DateRange(thisStartDt,thisNextDt);
        return dr;
    }
    public Boolean overlap(PeriodOfDay other){
        DateRange thisRange = getArbitraryDateRange(this);
        DateRange otherRange = getArbitraryDateRange(other);
        //start1.before(end2) && start2.before(end1);
        return thisRange.getStartDate().isBefore(otherRange.getEndDate()) && otherRange.getStartDate().isBefore(thisRange.getEndDate());
    }
}
