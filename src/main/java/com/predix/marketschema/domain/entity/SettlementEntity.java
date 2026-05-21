package com.predix.marketschema.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlements")
public class SettlementEntity {

    @Id
    private UUID id;

    @Column(name = "market_id", nullable = false)
    private UUID marketId;

    @Column(name = "outcome_id", nullable = false)
    private UUID outcomeId;

    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Column(name = "redeem_quantity", nullable = false, precision = 38, scale = 18)
    private BigDecimal redeemQuantity;

    @Column(name = "payout_amount", nullable = false, precision = 38, scale = 18)
    private BigDecimal payoutAmount;

    @Column(name = "payout_token", nullable = false, length = 32)
    private String payoutToken;

    @Column(name = "settlement_tx_hash", length = 128)
    private String settlementTxHash;

    @Column(name = "settled_at", nullable = false)
    private OffsetDateTime settledAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

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
