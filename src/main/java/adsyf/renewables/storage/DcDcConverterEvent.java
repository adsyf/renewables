package adsyf.renewables.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Slf4j
public class DcDcConverterEvent {
    ZonedDateTime startTime;
    BigDecimal eventDurationHours;
    BigDecimal inKwh;
    BigDecimal batKwh;
    BigDecimal drawKwh;
    BigDecimal gridKwh;
    BigDecimal effLossKwh;
    BigDecimal clipLossKwh;
    BigDecimal inTtlKwh;
    BigDecimal batTtlKwh;
    BigDecimal drawTtlKwh;
    BigDecimal gridTtlKwh;
    BigDecimal effLossTtlKwh;
    BigDecimal clipLossTtlKwh;
    BigDecimal errorInKw;
    BigDecimal errorMaxInKw;
}
