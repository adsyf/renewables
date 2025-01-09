package adsyf.renewables.v2.octopus;

public enum TariffUnitRateTypes {
    SUR("standard-unit-rates"),
    DUR("day-unit-rates"),
    NUR("night-unit-rates"),
    SCG("standing-charges");

    String code;

    TariffUnitRateTypes(String code){
        this.code=code;
    }
    public String getCode(){
        return this.code;
    }
}
