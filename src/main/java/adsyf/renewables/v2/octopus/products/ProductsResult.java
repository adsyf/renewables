package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProductsResult {
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
    @JsonProperty("full_name")
    String fullName;
    @JsonProperty("is_restricted")
    Boolean isRestricted;
    @JsonProperty("links")
    Link[] links;
    @JsonProperty("direction")
    String direction;
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
    public void print() throws JsonProcessingException {
        @Data
        class ProductResultSummary {
            String code;
            String displayName;
        }
        ProductResultSummary prs = new ProductResultSummary();
        prs.code = this.code;
        prs.displayName = this.displayName;
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(prs);
        System.out.println(jsonStr);
    }
}
