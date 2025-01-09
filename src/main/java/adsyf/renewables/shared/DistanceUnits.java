package adsyf.renewables.shared;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public enum DistanceUnits {
    MILLIMETRES,CENTIMETRES,METRES;

    public BigDecimal getMultiplier(DistanceUnits fromUnit, DistanceUnits toUnit) {
        BigDecimal multiplier = new BigDecimal(1);
        switch (fromUnit) {
            case MILLIMETRES:
                switch (toUnit) {
                    case MILLIMETRES:
                        multiplier = new BigDecimal(1);
                        break;
                    case CENTIMETRES:
                        multiplier = new BigDecimal("0.1");
                        break;
                    case METRES:
                        multiplier = new BigDecimal("0.01");
                        break;
                }
                break;
            case CENTIMETRES:
                switch (toUnit) {
                    case MILLIMETRES:
                        multiplier = new BigDecimal(10);
                        break;
                    case CENTIMETRES:
                        multiplier = new BigDecimal(1);
                        break;
                    case METRES:
                        multiplier = new BigDecimal("0.1");
                        break;
                }
                break;
            case METRES:
                switch (toUnit) {
                    case MILLIMETRES:
                        multiplier = new BigDecimal(100);
                        break;
                    case CENTIMETRES:
                        multiplier = new BigDecimal(10);
                        break;
                    case METRES:
                        multiplier = new BigDecimal(1);
                        break;
                }
                break;
        }
        return multiplier;
   }
}
