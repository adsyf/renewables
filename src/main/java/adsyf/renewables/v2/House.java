package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Data
@Slf4j
public class House {
    private String postcode;
    private EnergySupplier supplier;
    private String importProduct;
    private String exportProduct;
    private SolarInstallation solarInstall;
    public House(String postcode,EnergySupplier supplier,String importProduct, String exportProduct,SolarInstallation solarInstall){
        this.postcode = postcode;
        this.supplier = supplier;
        this.importProduct = importProduct;
        this.exportProduct = exportProduct;
        this.solarInstall = solarInstall;
    }
    //private ArrayList<EnergyPeriodPrice> getEnergyPrices(Period period){
    //    EnergySupplierPriceParameters params = new EnergySupplierPriceParameters();
    //    return this.supplier.getEnergyPrices(params,period);
    //}
    private EnergyEvent getUsage(Period period){
        return this.supplier.getUsage(period);
    }
    private ArrayList<EnergyEvent> getSolarGeneration(Period period){
        return this.solarInstall.getEvents(period);
    }
}
