package adsyf.renewables.v2.solar.forecasting.pvwatts.v8;

import adsyf.renewables.installation.MapLocation;
import adsyf.renewables.installation.RoofOrientation;
import adsyf.renewables.installation.SolarArray;
import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.config.ApiConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;

@Data
@Slf4j
public class Api {
    final ApiConfig apiConfig = new ApiConfig();
    HashSet<SolarArray> solarArrays = new HashSet<>();

    public void setOutputs(){
        for (SolarArray sa:solarArrays){
            setArrayOutputs(sa);
        }
    }
    private void setArrayOutputs(SolarArray sa){
        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString(apiConfig.getEndpoint() + "." + apiConfig.getFormat())
                .queryParam("system_capacity",sa.getCapacityKW())
                .queryParam("module_type",sa.getModuleType().getPvwattsV8Option())
                .queryParam("losses",sa.getLosses())
                .queryParam("array_type",sa.getArrayType().getPvwattsV8Option())
                .queryParam("tilt",sa.getSolarDirection().getTilt())
                .queryParam("azimuth",sa.getSolarDirection().getAzimuth())
                .queryParam("lat",sa.getLocation().getLatitude())
                .queryParam("lon",sa.getLocation().getLongitude())
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

        sa.setProjections(Objects.requireNonNull(resSpec.body(SolarDetails.class)).getOutputs());
    }
    public static void main(String[] args){
        HashSet<SolarArray> solarArrays = new HashSet<>();

        SolarArray seSa = new SolarArray();
        seSa.setSolarDirection(new RoofOrientation(new BigDecimal("23"),new BigDecimal("120")));
        seSa.setLocation(new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18")));
        seSa.setCapacityKW(new BigDecimal("4"));
        seSa.setName("49 Newmarket Road");
        solarArrays.add(seSa);

        SolarArray nwSa = new SolarArray();
        nwSa.setSolarDirection(new RoofOrientation(new BigDecimal("23"),new BigDecimal("300")));
        nwSa.setLocation(new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18")));
        nwSa.setCapacityKW(new BigDecimal("4"));
        nwSa.setName("49 Newmarket Road");
        solarArrays.add(nwSa);

        Api api = new Api();
        api.setSolarArrays(solarArrays);
        api.setOutputs();
        //log.info("normal year hours {}, leap year hours {}", TimeBlock.HOURS_IN_NORMAL_YEAR,TimeBlock.HOURS_IN_LEAP_YEAR);

        for (SolarArray sa2:solarArrays){
            log.info("{} annual ac projection is {}",sa2.getDescription(),sa2.getProjections().getAcAnnualKWh());
            //BigDecimal sum = Arrays.stream(sa2.getProjections().getAcWh()).reduce(BigDecimal.ZERO,BigDecimal::add);
            //log.info("got annual ac by sum of hours {}",sum.divide(BigDecimal.valueOf(1000)));
            //log.info("got {} ac hours",sa2.getProjections().getAcWh().length);
        }

    }

}
