package adsyf.renewables.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Slf4j
public class BatteryEvent {
    ZonedDateTime startTime;
    BatteryActionType action;
    BigDecimal eventDurationHours;
    BigDecimal energyKwh;
    BigDecimal socBeforeEvent;
    BigDecimal socAfterEvent;
}
