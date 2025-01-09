package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class MapLocation {
    private BigDecimal latitude;
    private BigDecimal longitude;
    public MapLocation(BigDecimal latitude, BigDecimal longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
