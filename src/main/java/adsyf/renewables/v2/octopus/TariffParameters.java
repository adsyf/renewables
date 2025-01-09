package adsyf.renewables.v2.octopus;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TariffParameters {
    String productCode;
    PaymentType paymentType;
    String postcode;
    PaymentMethod paymentMethod;
    public TariffParameters(){
    }
    public TariffParameters(String productCode,PaymentType paymentType,String postcode, PaymentMethod paymentMethod){
        this.productCode = productCode;
        this.paymentType = paymentType;
        this.postcode = postcode;
        this.paymentMethod = paymentMethod;
    }
}
