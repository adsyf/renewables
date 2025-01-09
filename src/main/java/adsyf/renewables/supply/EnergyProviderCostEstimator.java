package adsyf.renewables.supply;

import adsyf.renewables.shared.PeriodOfDay;

import java.util.HashMap;


public interface EnergyProviderCostEstimator {
    public HashMap<PeriodOfDay,Price> getElectBuyPricePerKwh(int monthOfYear,int dayOfMonth, PeriodOfDay pod);
    public HashMap<PeriodOfDay,Price> getElectSellPricePerKwh(int monthOfYear,int dayOfMonth, PeriodOfDay pod);

}
