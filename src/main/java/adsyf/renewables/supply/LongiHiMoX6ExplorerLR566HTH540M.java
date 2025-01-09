package adsyf.renewables.supply;

import adsyf.renewables.shared.DistanceUnits;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

@Data
@Slf4j
public class LongiHiMoX6ExplorerLR566HTH540M extends SolarPanel {
    public LongiHiMoX6ExplorerLR566HTH540M() throws MalformedURLException {
        url = new URL("https://static.longi.com/LR_5_66_HTH_520_540_M_35_35_and_15_Frame_Explorer_V19_72952af7a0.pdf");
        brand = SolarBrand.LONGI;
        height = new BigDecimal(2094);
        width = new BigDecimal(1134);
        depth = new BigDecimal(35);
        model = "LR5-66HTH-540M";
        range = "Hi-MO X6 Explorer";
        maxPower = new BigDecimal(540);
        units = DistanceUnits.MILLIMETRES;
    }
}
