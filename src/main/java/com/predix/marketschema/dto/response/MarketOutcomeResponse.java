package com.predix.marketschema.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MarketOutcomeResponse {

    private UUID id;
    private UUID marketId;
    private String outcomeCode;
    private Integer outcomeIndex;
    private String outcomeLabel;
    private Boolean isWinning;
    private BigDecimal payoutNumerator;
    private BigDecimal payoutDenominator;
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMarketId() {
        return marketId;
    }

    public void setMarketId(UUID marketId) {
        this.marketId = marketId;
    }

    public String getOutcomeCode() {
        return outcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public Integer getOutcomeIndex() {
        return outcomeIndex;
    }

    public void setOutcomeIndex(Integer outcomeIndex) {
        this.outcomeIndex = outcomeIndex;
    }

    public String getOutcomeLabel() {
        return outcomeLabel;
    }

    public void setOutcomeLabel(String outcomeLabel) {
        this.outcomeLabel = outcomeLabel;
    }

    public Boolean getIsWinning() {
        return isWinning;
    }

    public void setIsWinning(Boolean winning) {
        isWinning = winning;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
