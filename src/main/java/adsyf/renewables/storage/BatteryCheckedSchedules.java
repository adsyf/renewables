package adsyf.renewables.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Data
@Slf4j
public class BatteryCheckedSchedules {
    HashSet<BatterySchedule> batterySchedules;
    public void addSchedule(BatterySchedule batterySchedule){
        for (BatterySchedule existingSchedule: batterySchedules){
            if (existingSchedule.getPeriodOfDay().overlap(batterySchedule.getPeriodOfDay())){
                throw new UnsupportedOperationException("battery schedules cannot overlap");
            }
        }
    }
}
