package adsyf.renewables.installation;

import adsyf.renewables.v2.solar.forecasting.pvwatts.v8.Api;
import com.opencsv.exceptions.CsvException;
import adsyf.renewables.demand.Meter;
import adsyf.renewables.demand.metering.providers.emporia.CsvRawDataImport;
import adsyf.renewables.storage.BatterySystem;
import adsyf.renewables.storage.DCCoupledBatterySystem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
@Data
@Slf4j
public class Installation {
    private HashSet<SolarArray> solarArrays = new HashSet<>();
    private ArrayList<BatterySystem> batteries;
    private ArrayList<DCCoupledBatterySystem> dcCoupledBatteries;
    private Meter meter;

    public void estimateYearlyMetrics(){
        Api api = new Api();
        for (SolarArray sa:solarArrays){
            api.setSolarArrays(solarArrays);
            api.setOutputs();
        }




    }

    public static void main(String[] args) throws IOException, CsvException {
        Installation install = new Installation();
        File file = new File("C:\\Users\\adsyf\\Downloads\\data-export-208020-2246416567614596069\\58A778-Newmarket_Road-1H.csv");
        CsvRawDataImport csvRawDataImport = new CsvRawDataImport();
        csvRawDataImport.hourlyReader(file);
        install.setMeter(csvRawDataImport.getVirtMeter());

        HashSet<SolarArray> solarArrays = new HashSet<>();
        String loc = "49 Newmarket Road";

        SolarArray seSa = new SolarArray();
        seSa.setSolarDirection(new RoofOrientation(new BigDecimal("23"),new BigDecimal("120")));
        seSa.setLocation(new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18")));
        seSa.setCapacityKW(new BigDecimal("4"));
        seSa.setName(loc);
        solarArrays.add(seSa);

        SolarArray nwSa = new SolarArray();
        nwSa.setSolarDirection(new RoofOrientation(new BigDecimal("23"),new BigDecimal("300")));
        nwSa.setLocation(new MapLocation(new BigDecimal("51.09"),new BigDecimal("-0.18")));
        nwSa.setCapacityKW(new BigDecimal("4"));
        nwSa.setName(loc);
        solarArrays.add(nwSa);

        install.setSolarArrays(solarArrays);
        DCCoupledBatterySystem bat = new DCCoupledBatterySystem();
        bat.setBrand("Tesla");
        bat.setModel("Powerwall 3");
        bat.setCapacityKwh(new BigDecimal("13.5"));
        bat.setMaxAcChargeKw(new BigDecimal(5));
        bat.setMaxAcDischargeKw(new BigDecimal(11));
        bat.setMaxDcInKw(new BigDecimal(11));
        bat.setMaxDcOutKw(new BigDecimal(5));



        ZonedDateTime t10am = ZonedDateTime.of(2024,11,1,10,0,0,0, ZoneId.of("GMT"));
        //bat.solarCharge(t10am,new BigDecimal(20),new BigDecimal(1));


        BigDecimal lostCharge = bat.charge(t10am, new BigDecimal(7), new BigDecimal(1));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t2 = ZonedDateTime.of(2024,11,1,11,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.charge(t2, new BigDecimal(7), new BigDecimal(1)));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t3 = ZonedDateTime.of(2024,11,1,12,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.charge(t3, new BigDecimal(7), new BigDecimal("0.5")));
        log.info("total lost charge {}",lostCharge);
        ZonedDateTime t4 = ZonedDateTime.of(2024,11,1,13,0,0,0, ZoneId.of("GMT"));
        lostCharge = lostCharge.add(bat.discharge(t3, new BigDecimal(9), new BigDecimal("0.5")));
        log.info("total lost charge {}",lostCharge);
        bat.printEvents();
    }

}
