package com.predix.marketschema.dto.request;

import com.predix.marketschema.domain.enums.MarketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class CreateMarketRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    private MarketType marketType;

    @NotNull
    private Integer chainId;

    @NotBlank
    private String collateralTokenSymbol;

    private String ctfConditionId;
    private String umaQuestionId;

    @NotNull
    private OffsetDateTime openTime;

    @NotNull
    private OffsetDateTime closeTime;

    @NotNull
    private OffsetDateTime resolveDeadline;

    @NotBlank
    private String createdBy;

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
}
