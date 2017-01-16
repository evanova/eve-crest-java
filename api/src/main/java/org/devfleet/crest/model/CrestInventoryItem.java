package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestInventoryItem extends CrestEntity {

    @JsonProperty
    private int flag;

    @JsonProperty
    private long quantity;

    @JsonProperty("type")
    private CrestItem item;

    public int getFlag() {
        return flag;
    }

    public long getQuantity() {
        return quantity;
    }

    public CrestItem getItem() {
        return item;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setItem(CrestItem item) {
        this.item = item;
    }
}
