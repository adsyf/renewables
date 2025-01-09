package adsyf.renewables.v2.solar.forecasting.pvwatts.v8.config;

import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.Format;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.Timeframe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix="pvwatts.v8")
public class ApiConfig {
    String endpoint="https://developer.nrel.gov/api/pvwatts/v8";
    Format format=Format.json;
    String apiKey="oi2h4YJtAYFvkR4F1mc5zp0noVyPfphUeo9g48dc";
    //BigDecimal systemCapacity;
    //ModuleType moduleType=ModuleType.STANDARD;
    //BigDecimal losses=new BigDecimal(14);
    //ArrayType arrayType=ArrayType.FRM;
    //RoofOrientation roofOrientation;
    //address
    //MapLocation mapLocation;
    //file_id
    //dataset
    //radius
    Timeframe timeframe=Timeframe.hourly;
    //dc_ac_ratio
    //gcr
    //inv_eff
    //bifaciality
    //albedo
    //use_wf_albedo
    //soiling
    //callback
}
