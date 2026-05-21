package com.predix.marketschema.domain.entity;

import com.predix.marketschema.domain.enums.MarketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "market_audit_logs")
public class MarketAuditLogEntity {

    @Id
    private UUID id;

    @Column(name = "market_id", nullable = false)
    private UUID marketId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 32)
    private MarketStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 32)
    private MarketStatus toStatus;

    @Column(nullable = false, length = 64)
    private String action;

    @Column(nullable = false, length = 128)
    private String actor;

    @Column(columnDefinition = "TEXT")
    private String detail;

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

    public MarketStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(MarketStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public MarketStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(MarketStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
