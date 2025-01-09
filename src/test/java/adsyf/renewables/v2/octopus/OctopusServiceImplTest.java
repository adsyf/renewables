package adsyf.renewables.v2.octopus;

import adsyf.renewables.v2.octopus.products.ListProductsParameters;
import adsyf.renewables.v2.octopus.products.Products;
import adsyf.renewables.v2.octopus.products.ProductsResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OctopusServiceImplTest {

    @Test
    void getChargesVAR() {
        OctopusServiceImpl octoImpl = new OctopusServiceImpl();
        TariffParameters params = new TariffParameters("VAR-22-11-01", PaymentType.directDebitMonthly,"RH106ND", PaymentMethod.DIRECT_DEBIT);
        Charges c = octoImpl.getCharges(params);
        assertTrue(true);
    }
    @Test
    void getChargesAGILE() {
        OctopusServiceImpl octoImpl = new OctopusServiceImpl();
        TariffParameters params = new TariffParameters("AGILE-24-10-01", PaymentType.directDebitMonthly,"RH106ND", PaymentMethod.DIRECT_DEBIT);
        Charges c = octoImpl.getCharges(params);
        assertTrue(true);
    }

    @Test
    void getProducts() throws JsonProcessingException {

        ListProductsParameters params = new ListProductsParameters();
        params.setBrand("OCTOPUS_ENERGY");
        Products prods = OctopusServiceImpl.getProducts(params);
        List<ProductsResult> products = Arrays.stream(prods.getResults()).toList();
        for (ProductsResult p:products){
            p.print();
        }
        assertTrue(prods.getCount().intValue() >0);
    }
}