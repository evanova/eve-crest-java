package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestMarketBulkOrder extends CrestMarketOrder {
    @JsonProperty
    private long type;
    @JsonProperty("stationID")
    private long stationId;

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getStationId ( ) {
        return stationId;
    }

    public void setStationId ( long stationId ) {
        this.stationId = stationId;
    }
}
