package adsyf.renewables.v2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Data
@Slf4j
public class EventStore {
    private ArrayList<EnergyEvent> events;
    public void printEvents(EventType type){

    }
}
