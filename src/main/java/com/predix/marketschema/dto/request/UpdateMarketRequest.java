package com.predix.marketschema.dto.request;

import java.time.OffsetDateTime;

public class UpdateMarketRequest {

    private String title;
    private String description;
    private String category;
    private String ctfConditionId;
    private String umaQuestionId;
    private OffsetDateTime openTime;
    private OffsetDateTime closeTime;
    private OffsetDateTime resolveDeadline;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCtfConditionId() {
        return ctfConditionId;
    }

    public void setCtfConditionId(String ctfConditionId) {
        this.ctfConditionId = ctfConditionId;
    }

    public String getUmaQuestionId() {
        return umaQuestionId;
    }

    public void setUmaQuestionId(String umaQuestionId) {
        this.umaQuestionId = umaQuestionId;
    }

    public OffsetDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(OffsetDateTime openTime) {
        this.openTime = openTime;
    }

    public OffsetDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(OffsetDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public OffsetDateTime getResolveDeadline() {
        return resolveDeadline;
    }

    public void setResolveDeadline(OffsetDateTime resolveDeadline) {
        this.resolveDeadline = resolveDeadline;
    }
}
