package com.predix.marketschema.dto.request;

public class LifecycleActionRequest {

    private String actor = "system";

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
