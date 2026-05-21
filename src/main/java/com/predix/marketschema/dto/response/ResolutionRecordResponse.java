package com.predix.marketschema.dto.response;

import com.predix.marketschema.domain.enums.ResolutionSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ResolutionRecordResponse {

    private UUID id;
    private UUID marketId;
    private ResolutionSource resolutionSource;
    private String umaRequestTxHash;
    private String umaAssertionId;
    private String proposedOutcomeCode;
    private Boolean disputed;
    private String finalOutcomeCode;
    private OffsetDateTime resolvedAt;
    private String resolverRef;
    private String rawPayload;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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
