package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class EnergySupplierPriceParameters {
    String postCode;
    String importProduct;
    String exportProduct;
}
