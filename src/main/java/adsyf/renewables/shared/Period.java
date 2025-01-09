package adsyf.renewables.shared;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;

@Data
@Slf4j
public class Period {
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
