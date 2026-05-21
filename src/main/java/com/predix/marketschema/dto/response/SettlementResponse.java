package com.predix.marketschema.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SettlementResponse {

    private UUID id;
    private UUID marketId;
    private UUID outcomeId;
    private String userId;
    private BigDecimal redeemQuantity;
    private BigDecimal payoutAmount;
    private String payoutToken;
    private String settlementTxHash;
    private OffsetDateTime settledAt;
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

    public UUID getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(UUID outcomeId) {
        this.outcomeId = outcomeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getRedeemQuantity() {
        return redeemQuantity;
    }

    public void setRedeemQuantity(BigDecimal redeemQuantity) {
        this.redeemQuantity = redeemQuantity;
    }

    public BigDecimal getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(BigDecimal payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    public String getPayoutToken() {
        return payoutToken;
    }

    public void setPayoutToken(String payoutToken) {
        this.payoutToken = payoutToken;
    }

    public String getSettlementTxHash() {
        return settlementTxHash;
    }

    public void setSettlementTxHash(String settlementTxHash) {
        this.settlementTxHash = settlementTxHash;
    }

    public OffsetDateTime getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(OffsetDateTime settledAt) {
        this.settledAt = settledAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
