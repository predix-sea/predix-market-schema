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
@Table(name = "market_outcomes")
public class MarketOutcomeEntity {

    @Id
    private UUID id;

    @Column(name = "market_id", nullable = false)
    private UUID marketId;

    @Column(name = "outcome_code", nullable = false, length = 32)
    private String outcomeCode;

    @Column(name = "outcome_index", nullable = false)
    private Integer outcomeIndex;

    @Column(name = "outcome_label", nullable = false, length = 256)
    private String outcomeLabel;

    @Column(name = "is_winning")
    private Boolean isWinning;

    @Column(name = "payout_numerator", precision = 38, scale = 18)
    private BigDecimal payoutNumerator;

    @Column(name = "payout_denominator", precision = 38, scale = 18)
    private BigDecimal payoutDenominator;

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
