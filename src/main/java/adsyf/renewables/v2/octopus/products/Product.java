package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Product extends ProductsResult {
    /* from ProductsResult
    @JsonProperty("available_from")
    String availableFrom;
    @JsonProperty("brand")
    String brand;
    @JsonProperty("code")
    String code;
    @JsonProperty("description")
    String description;
    @JsonProperty("display_name")
    String displayName;
    */
    @JsonProperty("dual_register_electricity_tariffs")
    ElectricityTariffs dualRegisterElectricityTariffs;
    /* from ProductsResult
    @JsonProperty("full_name")
    String fullName;
    @JsonProperty("is_restricted")
    Boolean isRestricted;
    @JsonProperty("links")
    Link[] links;
    */
    @JsonProperty("sample_consumption")
    SampleConsumption sampleConsumption;
    /*sample_quotes documentation doesn't match api*/
    @JsonProperty("single_register_electricity_tariffs")
    ElectricityTariffs singleRegisterElectricityTariffs;
    @JsonProperty("single_register_gas_tariffs")
    GasTariffs singleRegisterGasTariffs;
    @JsonProperty("tariffs_active_at")
    String tariffsActiveAt;
    /* from ProductsResult
    @JsonProperty("is_variable")
    Boolean isVariable;
    @JsonProperty("is_green")
    Boolean isGreen;
    @JsonProperty("is_tracker")
    Boolean isTracker;
    @JsonProperty("is_prepay")
    Boolean isPrepay;
    @JsonProperty("is_business")
    Boolean isBusiness;
    @JsonProperty("term")
    Integer term;
    @JsonProperty("available_to")
    String availableTo;

    /* not used but in ProductsResult
    @JsonProperty("direction")
    String direction;

     */
}
