package org.devfleet.crest.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestFitting extends CrestEntity {

    @JsonProperty
    private String href;

    @JsonProperty
    private CrestItem ship;

    @JsonProperty("items")
    private List<CrestInventoryItem> inventory;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private Long fittingID;

    public Long getFittingID() {
        return fittingID;
    }

    public void setFittingID(Long fittingID) {
        this.fittingID = fittingID;
    }

    public CrestItem getShip() {
        return ship;
    }

    public List<CrestInventoryItem> getInventory() {
        return inventory;
    }

    public void setShip(CrestItem ship) {
        this.ship = ship;
    }

    public void setInventory(List<CrestInventoryItem> inventory) {
        this.inventory = inventory;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
