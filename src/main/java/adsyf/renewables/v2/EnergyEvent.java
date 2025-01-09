package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Slf4j
public class EnergyEvent {
    private Period period;
    private BigDecimal kwhIn;
    private BigDecimal kwhOut;
    private Set<Loss> losses;
    private BigDecimal postEventSoc;
    private BigDecimal pricePerKwh;
    private EventType type;
    private String eventTypeSource;
    public Usage getUsage(){
        return new Usage(this.getPeriod(),this.kwhIn==null ? this.kwhOut : this.kwhIn);
    }
}
