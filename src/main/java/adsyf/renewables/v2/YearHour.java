package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

@Data
@Slf4j
public class YearHour {
    int hourOfYear;
    BigDecimal percentOfHour;
    final static BigDecimal DEC_2_PCNT = new BigDecimal(100);

    public YearHour(int hourOfYear,BigDecimal percentOfHour){
        this.hourOfYear = hourOfYear;
        this.percentOfHour = percentOfHour;
    }
    public int getDayOfYear(){
        return this.getHourOfYear()/24;
    }

    public LocalDateTime getDateWithArbitraryYear(){
        int doy = getDayOfYear() + 1;
        int hourOfDay = this.getHourOfYear() - ((doy - 1)*24);
        return Year.of(2001).atDay(doy).atTime(hourOfDay,0);
    }


    @Override
    public String toString(){
        LocalDateTime dt = getDateWithArbitraryYear();
        return this.hourOfYear + " (hourOfYear) (" + this.percentOfHour.multiply(DEC_2_PCNT) + "%) " + dt;
    }

    public static void main(String[] args){
        YearHour yh = new YearHour(48,new BigDecimal(1));
        log.info("yh {}",yh.toString());
    }
}
