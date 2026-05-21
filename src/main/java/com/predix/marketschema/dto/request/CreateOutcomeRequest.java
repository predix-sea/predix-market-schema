package com.predix.marketschema.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateOutcomeRequest {

    @NotBlank
    private String outcomeCode;

    @NotBlank
    private String outcomeLabel;

    private Integer outcomeIndex;

    public String getOutcomeCode() {
        return outcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public String getOutcomeLabel() {
        return outcomeLabel;
    }

    public void setOutcomeLabel(String outcomeLabel) {
        this.outcomeLabel = outcomeLabel;
    }

    public Integer getOutcomeIndex() {
        return outcomeIndex;
    }

    public void setOutcomeIndex(Integer outcomeIndex) {
        this.outcomeIndex = outcomeIndex;
    }
}
