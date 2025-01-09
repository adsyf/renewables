package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
@Data
@Slf4j
public class PeriodUsage extends Period {

    HashSet<TimeBlock> gaps = new HashSet<>();
    BigDecimal totalKWh = new BigDecimal(0);

    public PeriodUsage(HashMap<Integer, Usage> powerUsed, ChronoUnit unit){
        powerUsed.values().removeIf(value -> !value.getTimeblock().getUnit().equals(unit));
        ArrayList<Usage> usages = new ArrayList<>(powerUsed.size());
        usages.addAll(powerUsed.values());
        Collections.sort(usages);
        this.setStartTime(usages.get(0).getTimeblock().getStartTime());
        this.setEndTime(usages.get(usages.size()-1).getTimeblock().getStartTime().plus(1l,unit));
        ZonedDateTime expectedPeriodStart = this.getStartTime();
        for (Usage usage: usages){
            this.totalKWh = this.getTotalKWh().add(usage.getKWh());
            if (usage.getTimeblock().getStartTime().equals(expectedPeriodStart)){
                expectedPeriodStart = usage.getTimeblock().getStartTime().plus(1L,unit);
            } else if (usage.getTimeblock().getStartTime().isAfter(expectedPeriodStart)) {
                while (usage.getTimeblock().getStartTime().isAfter(expectedPeriodStart)){
                    gaps.add(new TimeBlock(expectedPeriodStart,unit));
                    expectedPeriodStart = expectedPeriodStart.plus(1l,unit);
                }
                if (!usage.getTimeblock().getStartTime().equals(expectedPeriodStart)){
                    throw new ArithmeticException("block start " + usage.getTimeblock().getStartTime() + " not expected start time for block");
                }
            } else {
                throw new ArrayStoreException("block start " + usage.getTimeblock().getStartTime() + " before previous end " + expectedPeriodStart);
            }
        }


    }
    public void print(Boolean inclGaps){
        System.out.println(this.startTime + " -> " + this.endTime + "("+this.totalKWh+")");
        if (inclGaps) {
            for (TimeBlock gap : this.gaps) {
                System.out.println("gap block from: " + gap.getStartTime() + " until: " + gap.getStartTime().plus(1L, gap.getUnit()));
            }
        }
    }
    public void print(){
        print(Boolean.FALSE);
    }
    public static void main(String[] args){
        ZonedDateTime startTime1 = ZonedDateTime.of(2004,01,01,0,0,0,0, ZoneId.of("GMT"));
        TimeBlock tb = new TimeBlock(startTime1,ChronoUnit.HOURS);
        ZonedDateTime startTime2 = ZonedDateTime.of(2004,01,01,1,0,0,0, ZoneId.of("GMT"));
        ZonedDateTime startTime3 = ZonedDateTime.of(2004,01,01,4,0,0,0, ZoneId.of("GMT"));
        TimeBlock tb2 = new TimeBlock(startTime2,ChronoUnit.HOURS);
        TimeBlock tb3 = new TimeBlock(startTime1,ChronoUnit.DAYS);
        TimeBlock tb4= new TimeBlock(startTime3,ChronoUnit.HOURS);
        Usage u3 = new Usage(tb,new BigDecimal(1.4));
        Usage u2 = new Usage(tb2,new BigDecimal(1.5));
        Usage u1 = new Usage(tb3,new BigDecimal(10));
        Usage u4 = new Usage(tb4,new BigDecimal(3));
        HashMap<Integer,Usage> powerUsed = new HashMap<>();
        powerUsed.put(1,u1);
        powerUsed.put(2,u2);
        powerUsed.put(3,u3);
        powerUsed.put(4,u4);
        PeriodUsage period = new PeriodUsage(powerUsed,ChronoUnit.HOURS);
        period.print();
        //log.info("period start: {}, end: {}",period.getStartTime(),period.getEndTime());
    }
}
