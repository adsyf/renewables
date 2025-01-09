package adsyf.renewables.v2.solar.forecasting.pvwatts.v8;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ArrayType {
    //https://developer.nrel.gov/docs/solar/pvwatts/v8/ array_type
    FOR("Fixed - Open Rack",0),
    FRM("Fixed - Roof Mounted",1),
    OA("1-Axis",2),
    OAB("1-Axis Backtracking",3),
    TA("2-Aix",4);
    private final String description;
    private final int pvwattsV8Option;
    ArrayType(String description,int pvwattsV8Option){
        this.description=description;
        this.pvwattsV8Option=pvwattsV8Option;
    }
}
