package adsyf.renewables.v2.solar.forecasting.pvwatts.v8;

import lombok.Getter;

@Getter
public enum ModuleType {
    //https://developer.nrel.gov/docs/solar/pvwatts/v8/ module_type
    STANDARD("Standard",0),
    PREMIUM("Premium",1),
    THIN("Thin film",2);
    private final String description;
    private final int pvwattsV8Option;
    ModuleType(String description,int pvwattsV8Option){
        this.description=description;
        this.pvwattsV8Option=pvwattsV8Option;
    }
}
