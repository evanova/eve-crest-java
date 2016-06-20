package org.devfleet.crest.model;

public class CrestLocation extends CrestEntity {

    private CrestItem solarSystem;
    private CrestItem station;

    public CrestItem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(CrestItem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public CrestItem getStation() {
        return station;
    }

    public void setStation(CrestItem station) {
        this.station = station;
    }
}
