package adsyf.renewables.v2;

import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.ModuleType;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.config.ApiConfig;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.ArrayType;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.SolarDetails;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.response.Outputs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

@Data
@Slf4j
public class SolarArray {
    private final static ApiConfig apiConfig = new ApiConfig();
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
    private final MathContext V = new MathContext(5);
    private final BigDecimal KW_2_W = new BigDecimal(1000);

    public SolarArray(String name, MapLocation location, RoofOrientation solarDirection, BigDecimal capacityKW){
        this.name = name;
        this.location = location;
        this.solarDirection = solarDirection;
        this.capacityKW = capacityKW;
        this.losses = SolarArray.DEFAULT_LOSSES;
        this.arrayType = SolarArray.DEFAULT_ARRAY_TYPE;
        this.moduleType = SolarArray.DEFAULT_MODULE_TYPE;
        setArrayOutputs();
    }


    private void setArrayOutputs(){
        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString(apiConfig.getEndpoint() + "." + apiConfig.getFormat())
                .queryParam("system_capacity",this.capacityKW)
                .queryParam("module_type",this.moduleType.getPvwattsV8Option())
                .queryParam("losses",this.losses)
                .queryParam("array_type",this.arrayType.getPvwattsV8Option())
                .queryParam("tilt",this.solarDirection.getTilt())
                .queryParam("azimuth",this.solarDirection.getAzimuth())
                .queryParam("lat",this.location.getLatitude())
                .queryParam("lon",this.location.getLongitude())
                .queryParam("timeframe",apiConfig.getTimeframe())
                .build()
                .encode()
                .toUri();

        RestClient.ResponseSpec resSpec = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Key",apiConfig.getApiKey())
                .retrieve();

        //ResponseEntity<String> resEntity = resSpec.toEntity(String.class);
        //String solarDetailsStr = resSpec.body(String.class);
        this.projections = Objects.requireNonNull(resSpec.body(SolarDetails.class)).getOutputs();
    }

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
    public EnergyEvent getEvent(Period period){
        log.info("got {} hours of DC projections",this.projections.getDcWh().length);
        ArrayList<YearHour> yearHours = period.yearHoursBetween();
        EnergyEvent ee = new EnergyEvent();
        BigDecimal totalSolarDcWatts = BigDecimal.ZERO;
        for (YearHour yh:yearHours){
            BigDecimal dcWatts = this.projections.getDcWh()[yh.getHourOfYear()];
            log.debug("adding {} dcWatts {} ",yh.toString(),dcWatts);
            totalSolarDcWatts = totalSolarDcWatts.add(dcWatts.multiply(yh.percentOfHour,V));
        }
        log.info("totalSolarDcWatts {} kwOut {}",totalSolarDcWatts,totalSolarDcWatts.divide(KW_2_W,V));
        ee.setKwhOut(totalSolarDcWatts.divide(KW_2_W,V));
        ee.setType(EventType.SOLAR);
        ee.setEventTypeSource(this.getDescription());
        return ee;
    }
}
