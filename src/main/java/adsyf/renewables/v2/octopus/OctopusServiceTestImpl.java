package adsyf.renewables.v2.octopus;

import adsyf.renewables.v2.octopus.products.HistoricalCharge;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeSet;

@Slf4j
public class OctopusServiceTestImpl implements OctopusService {

    private boolean supportedPostCode(String postcode){
        boolean supported=false;
        if (postcode.equals("RH106ND")){
            supported= true;
        } else {
            log.error("unsupported test postcode {}",postcode);
        }
        return supported;
    }
    private boolean supportedProductCode(String productCode){
        boolean supported=false;
        if (productCode.equals("VAR-22-11-01")){
            supported= true;
        } else {
            log.error("unsupported test productCode {}",productCode);
        }
        return supported;
    }

    private boolean supportedPaymentType(PaymentType paymentType){
        boolean supported=false;
        if (paymentType.equals(PaymentType.directDebitMonthly)){
            supported= true;
        } else {
            log.error("unsupported test paymentType {}",paymentType);
        }
        return supported;
    }


    private void testParams(TariffParameters params){
        if (!(supportedPostCode(params.getPostcode())
                && supportedProductCode(params.getProductCode())
                && supportedPaymentType(params.getPaymentType())
           )){
          throw new RuntimeException("unsupported param for testing");
        }
    }
    @Override
    public Charges getCharges(TariffParameters params) {
        testParams(params);
        TreeSet<HistoricalCharge> standingCharges = null;
        TreeSet<HistoricalCharge> standardUnitRates = null;
        TreeSet<HistoricalCharge> dayUnitRates = null;
        TreeSet<HistoricalCharge> nightUnitRates = null;
        return new Charges(standingCharges,standardUnitRates,dayUnitRates,nightUnitRates);
    }
}
