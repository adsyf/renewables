package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.beans.JavaBean;

@Data
@Slf4j
public class Link {
    @JsonProperty("href")
    String href;
    @JsonProperty("method")
    String method;
    @JsonProperty("rel")
    String rel;
}
