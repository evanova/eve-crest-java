package org.devfleet.crest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrestMarketGroup extends CrestItem {

    @JsonProperty
    CrestItem types;
    @JsonProperty
    CrestItem parentGroup;

    public String getTypeRef() {
        return types.getHref();
    }

    public boolean hasParent() {
        return parentGroup != null;
    }

    public long getParentId() {
        if (hasParent()) {
            return parentGroup.getId();
        }

        return 0;
    }

    public String getParentRef() {
        if (hasParent()) {
            return parentGroup.getHref();
        }

        return "";
    }
}
