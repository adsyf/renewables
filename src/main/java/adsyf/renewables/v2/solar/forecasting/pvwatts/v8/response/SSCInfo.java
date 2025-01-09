package adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSCInfo {
    @JsonProperty("version")
    String version;
    @JsonProperty("build")
    String build;
    @JsonProperty("module")
    String module;
}
