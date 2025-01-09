package adsyf.renewables.storage;

import adsyf.renewables.shared.PeriodOfDay;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BatterySchedule {
    PeriodOfDay periodOfDay;
    BatteryActionType batteryScheduleType;
}
