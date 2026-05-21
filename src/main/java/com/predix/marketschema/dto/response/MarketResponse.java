package com.predix.marketschema.dto.response;

import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MarketResponse {

    private UUID id;
    private String marketCode;
    private String title;
    private String description;
    private String category;
    private MarketType marketType;
    private Integer chainId;
    private String collateralTokenSymbol;
    private String ctfConditionId;
    private String umaQuestionId;
    private MarketStatus status;
    private OffsetDateTime openTime;
    private OffsetDateTime closeTime;
    private OffsetDateTime resolveDeadline;
    private String createdBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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
