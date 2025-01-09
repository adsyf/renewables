package adsyf.renewables.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
    public HomeServiceImpl(){
    }

    @Override
    public String calculate() {
        //ApiParameters params = new ApiParameters();
        /*params.setRoofOrientation(new RoofOrientation(new BigDecimal("23"),new BigDecimal("119")));
        params.setMapLocation(new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18")));
        params.setSystemCapacity(new BigDecimal("3.6"));
        SolarDetails solarDetails = Api.getSolarDetails(params);
        log.info("got annual ac {}",solarDetails.getOutputs().getAcAnnualKWh());
        BigDecimal sum = Arrays.stream(solarDetails.getOutputs().getAcWh()).reduce(BigDecimal.ZERO,BigDecimal::add);
        log.info("got annual ac by sum of hours {}",sum.divide(BigDecimal.valueOf(1000)));
        log.info("got {} ac hours",solarDetails.getOutputs().getAcWh().length);
        log.info("normal year hours {}, leap year hours {}", TimeBlock.HOURS_IN_NORMAL_YEAR,TimeBlock.HOURS_IN_LEAP_YEAR);*/
        return "mycalc";
    }
}