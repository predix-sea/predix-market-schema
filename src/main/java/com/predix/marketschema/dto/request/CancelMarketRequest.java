package com.predix.marketschema.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CancelMarketRequest {

    @NotBlank
    private String actor;

    private String reason;
    private Boolean force = false;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }
}
