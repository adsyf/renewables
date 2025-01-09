package adsyf.renewables.components;

import java.math.BigDecimal;

public interface Inverter extends ActiveElectronics {
    public BigDecimal getDcAcEff();
    public BigDecimal getDcUsedKWh(BigDecimal acRequestedKwh);
    public BigDecimal getAcOutKWh(BigDecimal dcInKwh);
    public BigDecimal getInvMaxDcKw();
    public BigDecimal getInvMaxAcKw();
}
