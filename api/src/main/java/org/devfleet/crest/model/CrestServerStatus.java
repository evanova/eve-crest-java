package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CrestServerStatus extends CrestEntity {

    @JsonProperty
    private String serverName;

    @JsonProperty
    private String serverVersion;

    @JsonProperty
    private int userCount;

    @JsonProperty
    private String serviceStatus;

    public String getServerName() {
        return serverName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getCount() {
        return userCount;
    }

    public boolean getOnline() {
        return getOnlineBool();
    }

    private boolean getOnlineBool() {
        return serviceStatus.equalsIgnoreCase("online");
    }
}
