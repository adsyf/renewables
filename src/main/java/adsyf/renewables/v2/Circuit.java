package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Data
@Slf4j
public class Circuit {
    boolean isMeterTotal;
    String name;
    String type;
    //array should include all hours in year
    TreeSet<Usage> acKwh = new TreeSet<Usage>();
}
