package com.predix.marketschema.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ResolveMarketRequest {

    @NotBlank
    private String winningOutcomeCode;

    @NotBlank
    private String actor;

    private BigDecimal payoutNumerator;
    private BigDecimal payoutDenominator;

    public String getWinningOutcomeCode() {
        return winningOutcomeCode;
    }

    public void setWinningOutcomeCode(String winningOutcomeCode) {
        this.winningOutcomeCode = winningOutcomeCode;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public BigDecimal getPayoutNumerator() {
        return payoutNumerator;
    }

    public void setPayoutNumerator(BigDecimal payoutNumerator) {
        this.payoutNumerator = payoutNumerator;
    }

    public BigDecimal getPayoutDenominator() {
        return payoutDenominator;
    }

    public void setPayoutDenominator(BigDecimal payoutDenominator) {
        this.payoutDenominator = payoutDenominator;
    }
}
