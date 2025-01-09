package adsyf.renewables.v2;

import java.util.Map;

public interface EnergySupplier {
    ;
    public void putEnergyUsage(Map<Integer, Circuit> circuits);
    public EnergyEvent getUsage(Period period);
    public Price getPriceOfElectricity(Usage usage);

    public Price getPriceOfElectricity(Period period);

}
