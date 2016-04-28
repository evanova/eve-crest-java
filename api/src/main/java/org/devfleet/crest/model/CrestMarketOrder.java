package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CrestMarketOrder extends CrestItem {

    @JsonProperty("buy")
    private boolean isBuyOrder;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private long issued;

    @JsonProperty
    private double price;

    @JsonProperty
    private long volumeEntered;

    @JsonProperty
    private long minVolume;

    @JsonProperty
    private long volume;

    @JsonProperty
    private String range;

    @JsonProperty
    private CrestItem location;

    @JsonProperty
    private long duration;

    @JsonProperty
    private CrestItem type;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getIssued() {
        return issued;
    }

    public void setIssued(long issued) {
        this.issued = issued;
    }

    public long getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(long minVolume) {
        this.minVolume = minVolume;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getVolumeEntered() {
        return volumeEntered;
    }

    public void setVolumeEntered(long volumeEntered) {
        this.volumeEntered = volumeEntered;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getLocationName() {
        return this.location.getName();
    }

    public long getLocationId() {
        return this.location.getId();
    }

    public String getTypeName() {
        return this.type.getName();
    }

    public long getTypeId() {
        return this.type.getId();
    }
}
