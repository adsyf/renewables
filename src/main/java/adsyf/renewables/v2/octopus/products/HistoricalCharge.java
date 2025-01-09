package adsyf.renewables.v2.octopus.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import adsyf.renewables.v2.Period;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Slf4j
public class HistoricalCharge implements Comparable<HistoricalCharge> {
    //@JsonIgnore
    //private static final String NULL_VALID_TO="2100-12-31T00:00:00Z";
    @JsonIgnore
    ZonedDateTime validFrom;
    @JsonIgnore
    ZonedDateTime validTo;
    @JsonIgnore
    private final ZonedDateTime DFT_TO_DATE = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusYears(1);
    @JsonIgnore
    private final String DFT_PAYMENT_METHOD = "ALL";
    @JsonProperty("payment_method")
    String paymentMethod;
    @JsonProperty("valid_from")
    String valid_from;
    @JsonProperty("valid_to")
    String valid_to;
    @JsonProperty("value_exc_vat")
    BigDecimal value_exc_vat;
    @JsonProperty("value_inc_vat")
    BigDecimal value_inc_vat;
    public HistoricalCharge(){
    }
    public HistoricalCharge(Period period){
        this.setPeriod(period);
    }

    public HistoricalCharge(String isoLocalDateTimeStart, String isoLocalDateTimeEnd, String timeZone, String paymentMethod, String valueIncVat) {
        this(isoLocalDateTimeStart, isoLocalDateTimeEnd, ZoneId.of(timeZone), paymentMethod, valueIncVat);
    }
    public HistoricalCharge(String isoLocalDateTimeStart, String isoLocalDateTimeEnd, ZoneId zoneId, String paymentMethod, String valueIncVat) {
        this.validFrom = LocalDateTime.parse(isoLocalDateTimeStart).atZone(zoneId);
        this.validTo = LocalDateTime.parse(isoLocalDateTimeEnd).atZone(zoneId);
        this.paymentMethod = paymentMethod;
        this.value_inc_vat = new BigDecimal(valueIncVat);
    }
    public void setPeriod(Period period){
        this.validFrom = period.getStartTime();
        this.validTo = period.getEndTime();
    }
    public Period getPeriod(){
        return new Period(validFrom,validTo);
    }

    public void setValid_to(String valid_to){
        if (valid_to==null){
            this.validTo = DFT_TO_DATE;
            //this.valid_to = NULL_VALID_TO;
        } else {
            this.validTo = ZonedDateTime.ofInstant(Instant.parse(valid_to), ZoneId.systemDefault());
            this.valid_to = valid_to;
        }
    }

    public void setPaymentMethod(String paymentMethod){
        if (paymentMethod == null) {
            this.paymentMethod = this.DFT_PAYMENT_METHOD;
        } else {
            this.paymentMethod = paymentMethod;
        }
    }

    public void setValid_from(String valid_from){
        if (valid_from==null){
            throw new RuntimeException("cannot have null valid_from");
        } else {
            this.valid_from = valid_from;
            this.validFrom = ZonedDateTime.ofInstant(Instant.parse(valid_from), ZoneId.systemDefault());
        }
    }

    @Override
    public int compareTo(HistoricalCharge o) {
        if (this.validFrom.compareTo(o.validFrom) != 0){
            return this.validFrom.compareTo(o.validFrom);
        } else if (this.validTo.compareTo(o.validTo) != 0) {
            return this.validTo.compareTo(o.validTo);
        } else {
            return this.paymentMethod.compareTo(o.paymentMethod);
        }
    }

    public static void main(String[] args){
        String start = "2100-12-31T00:00:00Z";

        ZonedDateTime thisStart = ZonedDateTime.ofInstant(Instant.parse(start), ZoneId.systemDefault());
        log.info("done");


    }


}
