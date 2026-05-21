package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.entity.SettlementEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.dto.request.CreateSettlementRequest;
import com.predix.marketschema.dto.response.SettlementResponse;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.MarketOutcomeRepository;
import com.predix.marketschema.repository.SettlementRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final MarketOutcomeRepository outcomeRepository;
    private final MarketService marketService;
    private final EntityMapper mapper;

    public SettlementService(
            SettlementRepository settlementRepository,
            MarketOutcomeRepository outcomeRepository,
            MarketService marketService,
            EntityMapper mapper) {
        this.settlementRepository = settlementRepository;
        this.outcomeRepository = outcomeRepository;
        this.marketService = marketService;
        this.mapper = mapper;
    }

    @Transactional
    public SettlementResponse createSettlement(UUID marketId, CreateSettlementRequest request) {
        MarketEntity market = marketService.findMarketOrThrow(marketId);
        if (market.getStatus() != MarketStatus.RESOLVED && market.getStatus() != MarketStatus.SETTLED) {
            throw new BusinessException(
                    ErrorCode.MARKET_RULE_VIOLATION,
                    "Settlements require market in RESOLVED or SETTLED status");
        }

        MarketOutcomeEntity outcome = outcomeRepository.findById(request.getOutcomeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Outcome not found"));
        if (!outcome.getMarketId().equals(marketId)) {
            throw new BusinessException(ErrorCode.MARKET_RULE_VIOLATION, "Outcome does not belong to market");
        }

        SettlementEntity settlement = new SettlementEntity();
        settlement.setMarketId(marketId);
        settlement.setOutcomeId(request.getOutcomeId());
        settlement.setUserId(request.getUserId());
        settlement.setRedeemQuantity(request.getRedeemQuantity());
        settlement.setPayoutAmount(request.getPayoutAmount());
        settlement.setPayoutToken(request.getPayoutToken());
        settlement.setSettlementTxHash(request.getSettlementTxHash());
        settlement.setSettledAt(request.getSettledAt() != null ? request.getSettledAt() : OffsetDateTime.now());

        return mapper.toSettlementResponse(settlementRepository.save(settlement));
    }

    @Transactional(readOnly = true)
    public List<SettlementResponse> listSettlements(UUID marketId) {
        marketService.findMarketOrThrow(marketId);
        return settlementRepository.findByMarketIdOrderBySettledAtDesc(marketId).stream()
                .map(mapper::toSettlementResponse)
                .toList();
    }
}
