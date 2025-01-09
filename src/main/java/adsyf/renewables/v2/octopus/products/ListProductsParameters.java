package adsyf.renewables.v2.octopus.products;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;

@Data
@Slf4j
public class ListProductsParameters {
    ZonedDateTime availableAt;
    String brand;
    Boolean isBusiness;
    Boolean isGreen;
    Boolean isPrepay;
    Boolean isTracker;
    Boolean isVariable;
    Integer page;
}
