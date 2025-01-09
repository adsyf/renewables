package adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Inputs {
    @JsonProperty("system_capacity")
    BigDecimal systemCapacity;
    @JsonProperty("module_type")
    Integer moduleType;
    @JsonProperty("losses")
    BigDecimal losses;
    @JsonProperty("array_type")
    Integer arrayType;
    @JsonProperty("tilt")
    BigDecimal tilt;
    @JsonProperty("azimuth")
    BigDecimal azimuth;
    //address
    @JsonProperty("lat")
    BigDecimal latitude;
    @JsonProperty("lon")
    BigDecimal longitude;
    //file_id
    //dataset
    //radius
    @JsonProperty("timeframe")
    String timeframe;

}
