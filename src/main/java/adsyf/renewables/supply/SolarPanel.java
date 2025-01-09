package adsyf.renewables.supply;

import adsyf.renewables.shared.DistanceUnits;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URL;

@Data
@Slf4j
public class SolarPanel {
    URL url;
    SolarBrand brand;
    String model;
    BigDecimal height;
    BigDecimal width;
    BigDecimal depth;
    BigDecimal maxPower;
    String range;
    DistanceUnits units;
}
