package adsyf.renewables.v2.solar.forecasting.pvwatts.v8;

import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.Inputs;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.SSCInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.Outputs;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.StationInfo;
import lombok.Data;

@Data
public class SolarDetails {
    @JsonProperty("inputs")
    Inputs inputs;
    @JsonProperty("errors")
    String[] errors;
    @JsonProperty("warnings")
    String[] warnings;
    @JsonProperty("version")
    String version;
    @JsonProperty("ssc_info")
    SSCInfo sscInfo;
    @JsonProperty("station_info")
    StationInfo stationInfo;
    @JsonProperty("outputs")
    Outputs outputs;
}
