package adsyf.renewables.installation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MapLocation {
    BigDecimal latitude;
    BigDecimal longitude;
    public MapLocation(BigDecimal latitude,BigDecimal longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
