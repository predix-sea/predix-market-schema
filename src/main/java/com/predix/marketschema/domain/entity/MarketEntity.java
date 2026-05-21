package com.predix.marketschema.domain.entity;

import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
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

@Entity
@Table(name = "markets")
public class MarketEntity {

    @Id
    private UUID id;

    @Column(name = "market_code", nullable = false, unique = true, length = 64)
    private String marketCode;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 64)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_type", nullable = false, length = 32)
    private MarketType marketType = MarketType.BINARY;

    @Column(name = "chain_id", nullable = false)
    private Integer chainId;

    @Column(name = "collateral_token_symbol", nullable = false, length = 32)
    private String collateralTokenSymbol;

    @Column(name = "ctf_condition_id", length = 128)
    private String ctfConditionId;

    @Column(name = "uma_question_id", length = 128)
    private String umaQuestionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MarketStatus status;

    @Column(name = "open_time", nullable = false)
    private OffsetDateTime openTime;

    @Column(name = "close_time", nullable = false)
    private OffsetDateTime closeTime;

    @Column(name = "resolve_deadline", nullable = false)
    private OffsetDateTime resolveDeadline;

    @Column(name = "created_by", nullable = false, length = 128)
    private String createdBy;

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

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public Integer getChainId() {
        return chainId;
    }

    public void setChainId(Integer chainId) {
        this.chainId = chainId;
    }

    public String getCollateralTokenSymbol() {
        return collateralTokenSymbol;
    }

    public void setCollateralTokenSymbol(String collateralTokenSymbol) {
        this.collateralTokenSymbol = collateralTokenSymbol;
    }

    public String getCtfConditionId() {
        return ctfConditionId;
    }

    public void setCtfConditionId(String ctfConditionId) {
        this.ctfConditionId = ctfConditionId;
    }

    public String getUmaQuestionId() {
        return umaQuestionId;
    }

    public void setUmaQuestionId(String umaQuestionId) {
        this.umaQuestionId = umaQuestionId;
    }

    public MarketStatus getStatus() {
        return status;
    }

    public void setStatus(MarketStatus status) {
        this.status = status;
    }

    public OffsetDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(OffsetDateTime openTime) {
        this.openTime = openTime;
    }

    public OffsetDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(OffsetDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public OffsetDateTime getResolveDeadline() {
        return resolveDeadline;
    }

    public void setResolveDeadline(OffsetDateTime resolveDeadline) {
        this.resolveDeadline = resolveDeadline;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
