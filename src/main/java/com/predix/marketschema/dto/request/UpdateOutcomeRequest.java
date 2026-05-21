package com.predix.marketschema.dto.request;

import java.math.BigDecimal;

public class UpdateOutcomeRequest {

    private String outcomeLabel;
    private BigDecimal payoutNumerator;
    private BigDecimal payoutDenominator;

    public String getOutcomeLabel() {
        return outcomeLabel;
    }

    public void setOutcomeLabel(String outcomeLabel) {
        this.outcomeLabel = outcomeLabel;
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
