package adsyf.renewables.shared;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Usage implements Comparable<Usage> {
    TimeBlock timeblock;
    BigDecimal kWh;
    public Usage(TimeBlock timeblock,BigDecimal kWh){
        this.timeblock = timeblock;
        this.kWh = kWh;
    }
    @Override
    public int compareTo(Usage other){
        return this.timeblock.compareTo(other.getTimeblock());
    }

}
