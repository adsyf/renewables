package adsyf.renewables.demand;


import adsyf.renewables.supply.EnergyProviderCostEstimator;
import adsyf.renewables.shared.Usage;
import adsyf.renewables.installation.MapLocation;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
@Data
public class Meter {
    MapLocation mapLocation;
    String name;
    String mpan;
    String expMPAN;
    Integer fuseSize;
    EnergyProviderCostEstimator provider;
    Boolean economy7;
    Integer NoOfPhases = Integer.valueOf(0);
    HashSet<Circuit> l1Circuits = new HashSet<>();
    HashSet<Circuit> l2Circuits = new HashSet<>();;
    HashSet<Circuit> l3Circuits = new HashSet<>();;
    HashSet<Circuit> threePhaseCircuits = new HashSet<>();;
    //Integer is index hour of year i.e. 1st hour is 0
    HashMap<Integer, Usage> l1powerUsed;
    HashMap<Integer,Usage> l2powerUsed;
    HashMap<Integer,Usage> l3powerUsed;
    public Integer getNoOfPhases(){
        if (this.NoOfPhases.equals(0)){
            this.SetNoOfPhasesBasedOnUsage();
        }
        return this.NoOfPhases;
    }
    public void SetNoOfPhasesBasedOnUsage(){
        if (this.l1powerUsed != null && !this.l1powerUsed.isEmpty()){
            NoOfPhases++;
        }
        if (this.l2powerUsed != null && !this.l2powerUsed.isEmpty()){
            NoOfPhases++;
        }
        if (this.l3powerUsed != null && !this.l3powerUsed.isEmpty()){
            NoOfPhases++;
        }
    }
    public void setNextPhasePowerUsed(HashMap<Integer,Usage> phasePowerUsed){
        if (this.l1powerUsed == null){
            this.l1powerUsed = phasePowerUsed;
        } else if (this.l2powerUsed == null){
            this.l2powerUsed = phasePowerUsed;
        } else if (this.l3powerUsed == null){
            this.l3powerUsed = phasePowerUsed;
        } else {
            throw new UnsupportedOperationException("cannot have more than 3 phases");
        }
    }
}
