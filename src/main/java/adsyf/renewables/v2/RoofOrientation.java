package adsyf.renewables.v2;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoofOrientation {
    private BigDecimal tilt;
    private BigDecimal azimuth;
    public RoofOrientation(BigDecimal tilt, BigDecimal azimuth){
        this.tilt = tilt;
        this.azimuth = azimuth;
    }

    public String getApproxDirectionWord(){
        String direction;
        BigDecimal north0 = new BigDecimal("0");
        BigDecimal north360 = new BigDecimal("360");
        BigDecimal northNorthEast = new BigDecimal("22.5");
        BigDecimal northEast = new BigDecimal("45");
        BigDecimal eastNorthEast = new BigDecimal("67.5");
        BigDecimal east = new BigDecimal("90");
        BigDecimal eastSouthEast = new BigDecimal("112.5");
        BigDecimal southEast = new BigDecimal("135");
        BigDecimal southSouthEast = new BigDecimal("157.5");
        BigDecimal south = new BigDecimal("180");
        BigDecimal southSouthWest = new BigDecimal("202.5");
        BigDecimal southWest = new BigDecimal("225");
        BigDecimal westSouthWest = new BigDecimal("247.5");
        BigDecimal west = new BigDecimal("270");
        BigDecimal westNorthWest = new BigDecimal("292.5");
        BigDecimal northWest = new BigDecimal("315");
        BigDecimal northNorthWest = new BigDecimal("337.5");
        if (this.azimuth.compareTo(northNorthWest) >= 1 && this.azimuth.compareTo(north360) < 1){
            direction = "North";
        } else if (this.azimuth.compareTo(north0) >= 1 && this.azimuth.compareTo(northNorthEast) < 1){
            direction = "North";
        } else if (this.azimuth.compareTo(northNorthEast) >= 1 && this.azimuth.compareTo(eastNorthEast) < 1){
            direction = "North East";
        } else if (this.azimuth.compareTo(eastNorthEast) >= 1 && this.azimuth.compareTo(eastSouthEast) < 1) {
            direction = "East";
        } else if (this.azimuth.compareTo(eastSouthEast) >= 1 && this.azimuth.compareTo(southSouthEast) < 1) {
            direction = "South East";
        } else if (this.azimuth.compareTo(southSouthEast) >= 1 && this.azimuth.compareTo(southSouthWest) < 1) {
            direction = "South";
        } else if (this.azimuth.compareTo(southSouthWest) >= 1 && this.azimuth.compareTo(westSouthWest) < 1) {
            direction = "South West";
        } else if (this.azimuth.compareTo(westSouthWest) >= 1 && this.azimuth.compareTo(westNorthWest) < 1) {
            direction = "West";
        } else if (this.azimuth.compareTo(westNorthWest) >= 1 && this.azimuth.compareTo(northNorthWest) < 1) {
            direction = "North West";
        } else {
            direction = "Unknown compass direction";
        }
        return direction;
    }
}
