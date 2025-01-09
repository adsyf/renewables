package adsyf.renewables.demand;

import adsyf.renewables.shared.Usage;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;

@Data
public class Circuit {
    String name;
    String type;
    Integer fuseSize;
    //Integer is index hour of year i.e. 1st hour is 0
    HashMap<Integer, Usage> powerUsed;
    HashSet<Device> devices;
}
