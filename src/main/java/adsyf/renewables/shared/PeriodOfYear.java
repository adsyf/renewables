package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@Slf4j
public class PeriodOfYear extends PeriodOfDay {
    int monthOfYear;
    int dayOfMonth;
    Boolean monthOfYearSet = Boolean.FALSE;
    Boolean dayOfMonthSet = Boolean.FALSE;

    public void setMonthOfYear(int monthOfYear){
        if (monthOfYear < 1){
            throw new UnsupportedOperationException("year must be >= 1");
        }
        if (monthOfYear > 12){
            throw new UnsupportedOperationException("year must be <= 12");
        }
        this.monthOfYear = monthOfYear;
        monthOfYearSet = Boolean.TRUE;
    }

    public void setDayOfMonth(int dayOfMonth){
        if (!monthOfYearSet){
            throw new UnsupportedOperationException("year must be set before day can be set");
        }
        if (dayOfMonth < 1){
            throw new UnsupportedOperationException("day must be >= 1");
        }
        Set<Integer> monthsWith30Days = Set.of(4,6,9,11);
        Set<Integer> monthsWith31Days = Set.of(1,3,5,7,8,10,12);
        if (this.monthOfYear == 2){
            if (dayOfMonth > 28){
                throw new UnsupportedOperationException("day must be <= 28");
            }
        } else if (monthsWith30Days.contains(monthOfYear)){
            if (dayOfMonth > 30){
                throw new UnsupportedOperationException("day must be <= 30");
            }
        } else if (monthsWith31Days.contains(monthOfYear)){
            if (dayOfMonth > 31){
                throw new UnsupportedOperationException("day must be <= 31");
            }
        }
        this.dayOfMonth = dayOfMonth;
    }

}
