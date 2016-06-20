package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CrestMarketBulkOrder extends CrestItem {
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
	private long stationID;

	@JsonProperty
	private long volume;

	@JsonProperty
	private String range;

	@JsonProperty
	private long minVolume;

	@JsonProperty
	private long duration;

	@JsonProperty
	private long type;

	public boolean isBuyOrder() {
		return isBuyOrder;
	}

	public void setBuyOrder(boolean isBuyOrder) {
		this.isBuyOrder = isBuyOrder;
	}

	public long getIssued() {
		return issued;
	}

	public void setIssued(long issued) {
		this.issued = issued;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getVolumeEntered() {
		return volumeEntered;
	}

	public void setVolumeEntered(long volumeEntered) {
		this.volumeEntered = volumeEntered;
	}

	public long getStationID() {
		return stationID;
	}

	public void setStationID(long stationID) {
		this.stationID = stationID;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public long getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(long minVolume) {
		this.minVolume = minVolume;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}
}
