package adsyf.renewables.demand;

import adsyf.renewables.shared.Usage;
import lombok.Data;

import java.util.HashMap;

@Data
public class Device {
    String name;
    //Integer is index hour of year i.e. 1st hour is 0
    HashMap<Integer, Usage> powerUsed;
}
