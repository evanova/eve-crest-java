package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestMarketType extends CrestItem {

    static class Type extends CrestItem {

        @JsonProperty
        private CrestItem icon;

        public String getIconHref() {
            return this.icon.getHref();
        }

    }

    @JsonProperty
    private CrestItem marketGroup;

    @JsonProperty
    private Type type;

    public String getTypeName() {
        return this.type.getName();
    }

    public String getTypeHref() {
        return this.type.getHref();
    }

    public long getTypeId() {
        return this.getId();
    }

    public String getGroupHref() {
        return this.marketGroup.getHref();
    }

    public long getGroupId() {
        return this.marketGroup.getId();
    }

    public String getTypeIcon() {
        return this.type.getIconHref();
    }

}
