package com.predix.marketschema.domain.entity;

import com.predix.marketschema.domain.enums.ResolutionSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "resolution_records")
public class ResolutionRecordEntity {

    @Id
    private UUID id;

    @Column(name = "market_id", nullable = false)
    private UUID marketId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution_source", nullable = false, length = 32)
    private ResolutionSource resolutionSource;

    @Column(name = "uma_request_tx_hash", length = 128)
    private String umaRequestTxHash;

    @Column(name = "uma_assertion_id", length = 128)
    private String umaAssertionId;

    @Column(name = "proposed_outcome_code", length = 32)
    private String proposedOutcomeCode;

    @Column(nullable = false)
    private Boolean disputed = false;

    @Column(name = "final_outcome_code", length = 32)
    private String finalOutcomeCode;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "resolver_ref", length = 128)
    private String resolverRef;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_payload", columnDefinition = "jsonb")
    private String rawPayload;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
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

    public ResolutionSource getResolutionSource() {
        return resolutionSource;
    }

    public void setResolutionSource(ResolutionSource resolutionSource) {
        this.resolutionSource = resolutionSource;
    }

    public String getUmaRequestTxHash() {
        return umaRequestTxHash;
    }

    public void setUmaRequestTxHash(String umaRequestTxHash) {
        this.umaRequestTxHash = umaRequestTxHash;
    }

    public String getUmaAssertionId() {
        return umaAssertionId;
    }

    public void setUmaAssertionId(String umaAssertionId) {
        this.umaAssertionId = umaAssertionId;
    }

    public String getProposedOutcomeCode() {
        return proposedOutcomeCode;
    }

    public void setProposedOutcomeCode(String proposedOutcomeCode) {
        this.proposedOutcomeCode = proposedOutcomeCode;
    }

    public Boolean getDisputed() {
        return disputed;
    }

    public void setDisputed(Boolean disputed) {
        this.disputed = disputed;
    }

    public String getFinalOutcomeCode() {
        return finalOutcomeCode;
    }

    public void setFinalOutcomeCode(String finalOutcomeCode) {
        this.finalOutcomeCode = finalOutcomeCode;
    }

    public OffsetDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(OffsetDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolverRef() {
        return resolverRef;
    }

    public void setResolverRef(String resolverRef) {
        this.resolverRef = resolverRef;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
