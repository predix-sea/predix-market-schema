package com.predix.marketschema.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateSettlementRequest {

    @NotNull
    private UUID outcomeId;

    @NotBlank
    private String userId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal redeemQuantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal payoutAmount;

    @NotBlank
    private String payoutToken;

    private String settlementTxHash;
    private OffsetDateTime settledAt;

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
}
