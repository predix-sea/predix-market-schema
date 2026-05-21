package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketAuditLogEntity;
import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.entity.OrderEntity;
import com.predix.marketschema.domain.entity.PositionEntity;
import com.predix.marketschema.domain.entity.ResolutionRecordEntity;
import com.predix.marketschema.domain.entity.SettlementEntity;
import com.predix.marketschema.dto.response.MarketOutcomeResponse;
import com.predix.marketschema.dto.response.MarketResponse;
import com.predix.marketschema.dto.response.OrderResponse;
import com.predix.marketschema.dto.response.PageResponse;
import com.predix.marketschema.dto.response.PositionResponse;
import com.predix.marketschema.dto.response.ResolutionRecordResponse;
import com.predix.marketschema.dto.response.SettlementResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public MarketResponse toMarketResponse(MarketEntity entity) {
        MarketResponse r = new MarketResponse();
        r.setId(entity.getId());
        r.setMarketCode(entity.getMarketCode());
        r.setTitle(entity.getTitle());
        r.setDescription(entity.getDescription());
        r.setCategory(entity.getCategory());
        r.setMarketType(entity.getMarketType());
        r.setChainId(entity.getChainId());
        r.setCollateralTokenSymbol(entity.getCollateralTokenSymbol());
        r.setCtfConditionId(entity.getCtfConditionId());
        r.setUmaQuestionId(entity.getUmaQuestionId());
        r.setStatus(entity.getStatus());
        r.setOpenTime(entity.getOpenTime());
        r.setCloseTime(entity.getCloseTime());
        r.setResolveDeadline(entity.getResolveDeadline());
        r.setCreatedBy(entity.getCreatedBy());
        r.setCreatedAt(entity.getCreatedAt());
        r.setUpdatedAt(entity.getUpdatedAt());
        return r;
    }

    public MarketOutcomeResponse toOutcomeResponse(MarketOutcomeEntity entity) {
        MarketOutcomeResponse r = new MarketOutcomeResponse();
        r.setId(entity.getId());
        r.setMarketId(entity.getMarketId());
        r.setOutcomeCode(entity.getOutcomeCode());
        r.setOutcomeIndex(entity.getOutcomeIndex());
        r.setOutcomeLabel(entity.getOutcomeLabel());
        r.setIsWinning(entity.getIsWinning());
        r.setPayoutNumerator(entity.getPayoutNumerator());
        r.setPayoutDenominator(entity.getPayoutDenominator());
        r.setCreatedAt(entity.getCreatedAt());
        return r;
    }

    public OrderResponse toOrderResponse(OrderEntity entity) {
        OrderResponse r = new OrderResponse();
        r.setId(entity.getId());
        r.setOrderCode(entity.getOrderCode());
        r.setMarketId(entity.getMarketId());
        r.setOutcomeId(entity.getOutcomeId());
        r.setSide(entity.getSide());
        r.setOrderType(entity.getOrderType());
        r.setPrice(entity.getPrice());
        r.setQuantity(entity.getQuantity());
        r.setFilledQuantity(entity.getFilledQuantity());
        r.setUserId(entity.getUserId());
        r.setStatus(entity.getStatus());
        r.setCreatedAt(entity.getCreatedAt());
        r.setUpdatedAt(entity.getUpdatedAt());
        return r;
    }

    public PositionResponse toPositionResponse(PositionEntity entity) {
        PositionResponse r = new PositionResponse();
        r.setId(entity.getId());
        r.setMarketId(entity.getMarketId());
        r.setOutcomeId(entity.getOutcomeId());
        r.setUserId(entity.getUserId());
        r.setQuantity(entity.getQuantity());
        r.setAvgCost(entity.getAvgCost());
        r.setUnrealizedPnl(entity.getUnrealizedPnl());
        r.setUpdatedAt(entity.getUpdatedAt());
        return r;
    }

    public ResolutionRecordResponse toResolutionResponse(ResolutionRecordEntity entity) {
        ResolutionRecordResponse r = new ResolutionRecordResponse();
        r.setId(entity.getId());
        r.setMarketId(entity.getMarketId());
        r.setResolutionSource(entity.getResolutionSource());
        r.setUmaRequestTxHash(entity.getUmaRequestTxHash());
        r.setUmaAssertionId(entity.getUmaAssertionId());
        r.setProposedOutcomeCode(entity.getProposedOutcomeCode());
        r.setDisputed(entity.getDisputed());
        r.setFinalOutcomeCode(entity.getFinalOutcomeCode());
        r.setResolvedAt(entity.getResolvedAt());
        r.setResolverRef(entity.getResolverRef());
        r.setRawPayload(entity.getRawPayload());
        r.setCreatedAt(entity.getCreatedAt());
        r.setUpdatedAt(entity.getUpdatedAt());
        return r;
    }

    public SettlementResponse toSettlementResponse(SettlementEntity entity) {
        SettlementResponse r = new SettlementResponse();
        r.setId(entity.getId());
        r.setMarketId(entity.getMarketId());
        r.setOutcomeId(entity.getOutcomeId());
        r.setUserId(entity.getUserId());
        r.setRedeemQuantity(entity.getRedeemQuantity());
        r.setPayoutAmount(entity.getPayoutAmount());
        r.setPayoutToken(entity.getPayoutToken());
        r.setSettlementTxHash(entity.getSettlementTxHash());
        r.setSettledAt(entity.getSettledAt());
        r.setCreatedAt(entity.getCreatedAt());
        return r;
    }

    public <T> PageResponse<T> toPageResponse(Page<?> page, List<T> content) {
        PageResponse<T> pr = new PageResponse<>();
        pr.setContent(content);
        pr.setPage(page.getNumber());
        pr.setSize(page.getSize());
        pr.setTotalElements(page.getTotalElements());
        pr.setTotalPages(page.getTotalPages());
        return pr;
    }
}
