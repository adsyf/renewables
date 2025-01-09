package adsyf.renewables.components;

import java.math.BigDecimal;


public interface Rectifier extends ActiveElectronics {
    public BigDecimal getAcDcEff();
    public BigDecimal getAcUsedKWh(BigDecimal dcRequestedKwh);
    public BigDecimal getDcOutKWh(BigDecimal acInKwh);
    public BigDecimal getRecMaxDcKw();
    public BigDecimal getRecMaxAcKw();

}
