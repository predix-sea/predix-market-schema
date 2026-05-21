package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
import com.predix.marketschema.dto.request.CreateOutcomeRequest;
import com.predix.marketschema.dto.request.UpdateOutcomeRequest;
import com.predix.marketschema.dto.response.MarketOutcomeResponse;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.MarketOutcomeRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutcomeService {

    private static final Set<String> BINARY_CODES = Set.of("YES", "NO");

    private final MarketOutcomeRepository outcomeRepository;
    private final MarketService marketService;
    private final MarketValidationService validationService;
    private final EntityMapper mapper;

    public OutcomeService(
            MarketOutcomeRepository outcomeRepository,
            MarketService marketService,
            MarketValidationService validationService,
            EntityMapper mapper) {
        this.outcomeRepository = outcomeRepository;
        this.marketService = marketService;
        this.validationService = validationService;
        this.mapper = mapper;
    }

    @Transactional
    public MarketOutcomeResponse addOutcome(UUID marketId, CreateOutcomeRequest request) {
        MarketEntity market = marketService.findMarketOrThrow(marketId);
        if (market.getStatus() != MarketStatus.DRAFT) {
            throw new BusinessException(ErrorCode.MARKET_RULE_VIOLATION, "Outcomes can only be added in DRAFT status");
        }

        String code = request.getOutcomeCode().toUpperCase();
        if (market.getMarketType() == MarketType.BINARY && !BINARY_CODES.contains(code)) {
            throw new BusinessException(ErrorCode.OUTCOME_RULE_VIOLATION, "Binary market outcomes must be YES or NO");
        }

        outcomeRepository.findByMarketIdAndOutcomeCode(marketId, code).ifPresent(o -> {
            throw new BusinessException(ErrorCode.OUTCOME_RULE_VIOLATION, "Outcome code already exists: " + code);
        });

        List<MarketOutcomeEntity> existing = outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(marketId);
        int index = request.getOutcomeIndex() != null ? request.getOutcomeIndex() : validationService.nextOutcomeIndex(existing);

        MarketOutcomeEntity outcome = new MarketOutcomeEntity();
        outcome.setMarketId(marketId);
        outcome.setOutcomeCode(code);
        outcome.setOutcomeIndex(index);
        outcome.setOutcomeLabel(request.getOutcomeLabel());

        MarketOutcomeEntity saved = outcomeRepository.save(outcome);
        return mapper.toOutcomeResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MarketOutcomeResponse> listOutcomes(UUID marketId) {
        marketService.findMarketOrThrow(marketId);
        return outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(marketId).stream()
                .map(mapper::toOutcomeResponse)
                .toList();
    }

    @Transactional
    public MarketOutcomeResponse updateOutcome(UUID outcomeId, UpdateOutcomeRequest request) {
        MarketOutcomeEntity outcome = outcomeRepository.findById(outcomeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Outcome not found: " + outcomeId));

        MarketEntity market = marketService.findMarketOrThrow(outcome.getMarketId());
        validationService.validateOutcomeModificationAllowed(market);

        if (request.getOutcomeLabel() != null) {
            outcome.setOutcomeLabel(request.getOutcomeLabel());
        }
        if (request.getPayoutNumerator() != null) {
            outcome.setPayoutNumerator(request.getPayoutNumerator());
        }
        if (request.getPayoutDenominator() != null) {
            outcome.setPayoutDenominator(request.getPayoutDenominator());
        }

        return mapper.toOutcomeResponse(outcomeRepository.save(outcome));
    }
}
