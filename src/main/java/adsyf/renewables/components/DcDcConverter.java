package adsyf.renewables.components;

import adsyf.renewables.storage.DcDcConverterEvent;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface DcDcConverter extends ActiveElectronics {
    public BigDecimal getDcDcEff();
    public BigDecimal getDcOutKWh(BigDecimal dcInKwh);
    public BigDecimal getMaxDcInKw();
    public BigDecimal getMaxDcOutKw();
    public ArrayList<DcDcConverterEvent> getDcDcConverterEvents();
    public void printDcDcConverterEvents();
}
