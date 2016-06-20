package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestMarketBulkOrder extends CrestMarketOrder {
    @JsonProperty
    private long type;

	public long getType() {
        return type;
	}

	public void setType(long type) {
        this.type = type;
	}
}
