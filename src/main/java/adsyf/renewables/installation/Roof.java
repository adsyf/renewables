package adsyf.renewables.installation;

import adsyf.renewables.shared.DistanceUnits;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class Roof {
    private BigDecimal length;
    private BigDecimal width;
    private DistanceUnits units;
    public static void main(String[] args){
        Roof roof = new Roof();
        roof.setLength(new BigDecimal("4.72"));
        roof.setWidth(new BigDecimal("8.7"));
        roof.units = DistanceUnits.METRES;
    }
}
