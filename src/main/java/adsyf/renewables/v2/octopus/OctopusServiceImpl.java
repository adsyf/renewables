package adsyf.renewables.v2.octopus;

import adsyf.renewables.v2.DistributionArea;
import adsyf.renewables.v2.octopus.industry.ListIndustryGridSupplyPointsResponse;
import adsyf.renewables.v2.octopus.products.*;
import io.brim.renewables.v2.octopus.products.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.TreeSet;

@Slf4j
public class OctopusServiceImpl implements OctopusService {
    final static String BASE="https://api.octopus.energy/v1/";
    RestClient restClient = RestClient.create();

    public static Products getProducts(ListProductsParameters listProductsParameters){
        String fnc = "products/";
        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString(BASE + fnc)
                .queryParam("brand", listProductsParameters.getBrand())
                .build()
                .encode()
                .toUri();

        RestClient.ResponseSpec resSpec = restClient.get()
                .uri(uri)
                .retrieve();

        //ResponseEntity<String> resEntity = resSpec.toEntity(String.class);

        //String products = resSpec.body(String.class);

        return resSpec.body(Products.class);
    }

    //productCode must end in forward slash
    public static Product getProduct(String productCode){
        String fnc = "products/";
        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString(BASE + fnc + productCode + "/")
                .build()
                .encode()
                .toUri();

        RestClient.ResponseSpec resSpec = restClient.get()
                .uri(uri)
                .retrieve();

        //ResponseEntity<String> resEntity = resSpec.toEntity(String.class);

        //String products = resSpec.body(String.class);

        return resSpec.body(Product.class);
    }
    public static String getGSPId(Integer page, String postcode){
        String fnc = "industry/grid-supply-points/";
        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString(BASE + fnc)
                .queryParam("postcode", postcode)
                .build()
                .encode()
                .toUri();

        RestClient.ResponseSpec resSpec = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();

        ResponseEntity<ListIndustryGridSupplyPointsResponse> resEntity = resSpec.toEntity(ListIndustryGridSupplyPointsResponse.class);

        //String products = resSpec.body(String.class);


        String groupId = "";
        if (resEntity.hasBody()){
            ListIndustryGridSupplyPointsResponse res = resEntity.getBody();
            if (res.getCount().intValue()<1){
                throw new RuntimeException("problems getting GSP Id, likely incorrect postcode " + postcode);
            } else {
                groupId= res.getResults()[0].getGroupId();
            }
        } else {
            throw new RuntimeException("problems getting GSP Id");
        }
        return groupId;
    }

    private ListTariffResponse getListTariffResponse(String uriStr){
        URI uri = UriComponentsBuilder
                .fromUriString(uriStr)
                .build()
                .encode()
                .toUri();
        RestClient.ResponseSpec resSpec = restClient.get()
                .uri(uri)
                .retrieve();
        try {
            return resSpec.body(ListTariffResponse.class);
        } catch (Exception e){

            if (e.getMessage().contains("This tariff has standard rates, not day and night")){
                return null;
            } else {
                throw e;
            }

        }

    }


    private TreeSet<HistoricalCharge> getHistoricalCharges(String uriStr){
        TreeSet<HistoricalCharge> setOfCharges = new TreeSet<>();
        boolean isNext=true;
        while (isNext){
            log.info("getting tariff for "+uriStr);
            ListTariffResponse listTariffResponse = getListTariffResponse(uriStr);
            if (listTariffResponse==null){
                isNext=false;
            } else {
                setOfCharges.addAll(Arrays.stream(listTariffResponse.getResults()).toList());
                if (listTariffResponse.getNext()==null){
                    isNext=false;
                } else {
                    uriStr = listTariffResponse.getNext();
                }
            }
        }
        return setOfCharges;
    }

    private TreeSet<HistoricalCharge> getChargesByPaymentMethod(TreeSet<HistoricalCharge> chargesWithAllPaymentMethods, PaymentMethod paymentMethod){
        TreeSet<HistoricalCharge> charges = new TreeSet<>();
        for (HistoricalCharge hc:chargesWithAllPaymentMethods){
            if (hc.getPaymentMethod().equals(PaymentMethod.ALL.name()) || hc.getPaymentMethod().equals(paymentMethod.name())){
                charges.add(hc);
            }
        }
        return charges;
    }

    @Override
    public Charges getCharges(TariffParameters params){
        String tariffCode = getTariffCode(params);
        String fnc = "products/";
        String fncT = "/electricity-tariffs/";

        String uriStr = BASE + fnc + params.getProductCode() + fncT + tariffCode + "/"+TariffUnitRateTypes.SCG.getCode()+"/";
        TreeSet<HistoricalCharge> standingCharges = getHistoricalCharges(uriStr);
        TreeSet<HistoricalCharge> stdCharges = getChargesByPaymentMethod(standingCharges, params.getPaymentMethod());

        uriStr = BASE + fnc + params.getProductCode() + fncT + tariffCode + "/"+TariffUnitRateTypes.SUR.getCode()+"/";
        TreeSet<HistoricalCharge> standardUnitRates = getHistoricalCharges(uriStr);
        TreeSet<HistoricalCharge> suRates = getChargesByPaymentMethod(standardUnitRates, params.getPaymentMethod());

        uriStr = BASE + fnc + params.getProductCode() + fncT + tariffCode + "/"+TariffUnitRateTypes.DUR.getCode()+"/";
        TreeSet<HistoricalCharge> dayUnitRates = getHistoricalCharges(uriStr);
        TreeSet<HistoricalCharge> duRates = getChargesByPaymentMethod(dayUnitRates, params.getPaymentMethod());

        uriStr = BASE + fnc + params.getProductCode() + fncT + tariffCode + "/"+TariffUnitRateTypes.NUR.getCode()+"/";
        TreeSet<HistoricalCharge> nightUnitRates = getHistoricalCharges(uriStr);
        TreeSet<HistoricalCharge> nuRates = getChargesByPaymentMethod(nightUnitRates, params.getPaymentMethod());

        return new Charges(stdCharges,suRates,duRates,nuRates);
    }
    private String getTariffCode(TariffParameters params){
        Product product = getProduct(params.getProductCode());
        String gspId = getGSPId(null, params.getPostcode());
        DistributionArea da = DistributionArea.getDistributionArea(gspId);
        ElectricityTariffPaymentMethods etpm = new ElectricityTariffPaymentMethods();
        switch (da.getGspId()) {
            case "_A" -> etpm = product.getSingleRegisterElectricityTariffs().getA();
            case "_B" -> etpm = product.getSingleRegisterElectricityTariffs().getB();
            case "_C" -> etpm = product.getSingleRegisterElectricityTariffs().getC();
            case "_D" -> etpm = product.getSingleRegisterElectricityTariffs().getD();
            case "_E" -> etpm = product.getSingleRegisterElectricityTariffs().getE();
            case "_F" -> etpm = product.getSingleRegisterElectricityTariffs().getF();
            case "_G" -> etpm = product.getSingleRegisterElectricityTariffs().getG();
            case "_H" -> etpm = product.getSingleRegisterElectricityTariffs().getH();
            case "_J" -> etpm = product.getSingleRegisterElectricityTariffs().getJ();
            case "_K" -> etpm = product.getSingleRegisterElectricityTariffs().getK();
            case "_L" -> etpm = product.getSingleRegisterElectricityTariffs().getL();
            case "_M" -> etpm = product.getSingleRegisterElectricityTariffs().getM();
            case "_N" -> etpm = product.getSingleRegisterElectricityTariffs().getN();
            case "_P" -> etpm = product.getSingleRegisterElectricityTariffs().getP();
        }
        ElectricityTariff et = new ElectricityTariff();
        if (params.getPaymentType().equals(PaymentType.directDebitMonthly)){
            et = etpm.getDirectDebitMonthly();
        } else if (params.getPaymentType().equals(PaymentType.directDebitQuarterly)) {
            et = etpm.getDirectDebitQuarterly();
        } else if (params.getPaymentType().equals(PaymentType.porob)) {
            et = etpm.getPorob();
        }  else if (params.getPaymentType().equals(PaymentType.prepayment)) {
            et = etpm.getPrepayment();
        }
        if (et==null){
            et = etpm.getVarying();
        }

        return et.getCode();
    }
    public static void main(String[] args){
        OctopusServiceImpl octoImpl = new OctopusServiceImpl();
        TariffParameters params = new TariffParameters("VAR-22-11-01", PaymentType.directDebitMonthly,"RH106ND", PaymentMethod.DIRECT_DEBIT);
        octoImpl.getCharges(params);
    }
}
