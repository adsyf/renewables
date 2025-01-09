package adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StationInfo {
    @JsonProperty("lat")
    BigDecimal latitude;
    @JsonProperty("lon")
    BigDecimal longitude;
    @JsonProperty("elav")
    BigDecimal elevation;
    @JsonProperty("tz")
    BigDecimal timezone;
    @JsonProperty("location")
    String location;
    @JsonProperty("city")
    String city;
    @JsonProperty("state")
    String state;
    @JsonProperty("country")
    String country;
    @JsonProperty("solar_resource_file")
    String solarResourceFile;
    @JsonProperty("distance")
    Integer distance;
    @JsonProperty("weather_data_source")
    String weatherDataSource;
}
