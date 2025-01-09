package adsyf.renewables.installation;

import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.ModuleType;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.ArrayType;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.Outputs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Slf4j
public class SolarArray {
    private String name;
    private MapLocation location;
    private RoofOrientation solarDirection;
    private BigDecimal capacityKW;
    private BigDecimal losses;
    private ArrayType arrayType;
    private ModuleType moduleType;
    final static BigDecimal DEFAULT_LOSSES = new BigDecimal(14);
    final static ArrayType DEFAULT_ARRAY_TYPE = ArrayType.FRM;
    final static ModuleType DEFAULT_MODULE_TYPE = ModuleType.STANDARD;
    private Outputs projections;

    public BigDecimal getLosses(){
        return Objects.requireNonNullElse(this.losses, DEFAULT_LOSSES);

    };
    public ArrayType getArrayType(){
        return Objects.requireNonNullElse(this.arrayType, DEFAULT_ARRAY_TYPE);
    };

    public ModuleType getModuleType(){
        return Objects.requireNonNullElse(this.moduleType, DEFAULT_MODULE_TYPE);
    };

    public String getDescription(){
        return this.name + " " + this.solarDirection.getApproxDirectionWord() + " " + this.capacityKW + " KW";
    }

}
