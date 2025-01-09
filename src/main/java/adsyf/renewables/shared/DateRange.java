package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
public class DateRange {
    LocalDateTime startDate;
    LocalDateTime endDate;
    public DateRange(LocalDateTime startDate,LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}