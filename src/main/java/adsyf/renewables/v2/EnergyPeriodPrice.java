package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Data
public class EnergyPeriodPrice implements Comparable<EnergyPeriodPrice> {
    private Period period;
    private EnergyPrice price;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyPeriodPrice epp = (EnergyPeriodPrice) o;
        return Objects.equals(period, epp.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    @Override
    public int compareTo(EnergyPeriodPrice epp) {
        return this.period.compareTo(epp.period);
    }
}
