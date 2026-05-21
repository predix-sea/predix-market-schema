package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
import com.predix.marketschema.dto.request.CancelMarketRequest;
import com.predix.marketschema.dto.request.CreateMarketRequest;
import com.predix.marketschema.dto.request.ResolveMarketRequest;
import com.predix.marketschema.dto.request.UpdateMarketRequest;
import com.predix.marketschema.dto.response.MarketResponse;
import com.predix.marketschema.dto.response.PageResponse;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.MarketOutcomeRepository;
import com.predix.marketschema.repository.MarketRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketOutcomeRepository outcomeRepository;
    private final MarketStateMachine stateMachine;
    private final MarketValidationService validationService;
    private final MarketAuditService auditService;
    private final MarketCodeGenerator codeGenerator;
    private final EntityMapper mapper;

    public MarketService(
            MarketRepository marketRepository,
            MarketOutcomeRepository outcomeRepository,
            MarketStateMachine stateMachine,
            MarketValidationService validationService,
            MarketAuditService auditService,
            MarketCodeGenerator codeGenerator,
            EntityMapper mapper) {
        this.marketRepository = marketRepository;
        this.outcomeRepository = outcomeRepository;
        this.stateMachine = stateMachine;
        this.validationService = validationService;
        this.auditService = auditService;
        this.codeGenerator = codeGenerator;
        this.mapper = mapper;
    }

    @Transactional
    public MarketResponse createMarket(CreateMarketRequest request) {
        validationService.validateTimeWindow(request.getOpenTime(), request.getCloseTime(), request.getResolveDeadline());

        MarketEntity market = new MarketEntity();
        market.setMarketCode(codeGenerator.generate());
        market.setTitle(request.getTitle());
        market.setDescription(request.getDescription());
        market.setCategory(request.getCategory());
        market.setMarketType(request.getMarketType() != null ? request.getMarketType() : MarketType.BINARY);
        market.setChainId(request.getChainId());
        market.setCollateralTokenSymbol(request.getCollateralTokenSymbol());
        market.setCtfConditionId(request.getCtfConditionId());
        market.setUmaQuestionId(request.getUmaQuestionId());
        market.setStatus(MarketStatus.DRAFT);
        market.setOpenTime(request.getOpenTime());
        market.setCloseTime(request.getCloseTime());
        market.setResolveDeadline(request.getResolveDeadline());
        market.setCreatedBy(request.getCreatedBy());

        MarketEntity saved = marketRepository.save(market);
        auditService.logTransition(saved.getId(), null, MarketStatus.DRAFT, "CREATE", request.getCreatedBy(), null);
        return mapper.toMarketResponse(saved);
    }

    @Transactional(readOnly = true)
    public MarketResponse getMarket(UUID id) {
        return mapper.toMarketResponse(findMarketOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<MarketResponse> listMarkets(MarketStatus status, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MarketEntity> result = marketRepository.findByFilters(status, category, pageable);
        List<MarketResponse> content = result.getContent().stream().map(mapper::toMarketResponse).toList();
        return mapper.toPageResponse(result, content);
    }

    @Transactional
    public MarketResponse updateMarket(UUID id, UpdateMarketRequest request) {
        MarketEntity market = findMarketOrThrow(id);
        validationService.assertNotSettled(market);
        if (market.getStatus() != MarketStatus.DRAFT) {
            throw new BusinessException(ErrorCode.MARKET_RULE_VIOLATION, "Only DRAFT markets can be updated");
        }

        if (request.getTitle() != null) {
            market.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            market.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            market.setCategory(request.getCategory());
        }
        if (request.getCtfConditionId() != null) {
            market.setCtfConditionId(request.getCtfConditionId());
        }
        if (request.getUmaQuestionId() != null) {
            market.setUmaQuestionId(request.getUmaQuestionId());
        }
        OffsetDateTime open = request.getOpenTime() != null ? request.getOpenTime() : market.getOpenTime();
        OffsetDateTime close = request.getCloseTime() != null ? request.getCloseTime() : market.getCloseTime();
        OffsetDateTime deadline = request.getResolveDeadline() != null ? request.getResolveDeadline() : market.getResolveDeadline();
        validationService.validateTimeWindow(open, close, deadline);
        market.setOpenTime(open);
        market.setCloseTime(close);
        market.setResolveDeadline(deadline);

        return mapper.toMarketResponse(marketRepository.save(market));
    }

    @Transactional
    public MarketResponse openMarket(UUID id, String actor) {
        return transition(id, MarketStatus.OPEN, "OPEN", actor, () -> {
            MarketEntity market = findMarketOrThrow(id);
            List<MarketOutcomeEntity> outcomes = outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(id);
            validationService.validateOutcomesForOpen(market, outcomes);
            validationService.validateTimeWindow(market.getOpenTime(), market.getCloseTime(), market.getResolveDeadline());
        });
    }

    @Transactional
    public MarketResponse closeMarket(UUID id, String actor) {
        return transition(id, MarketStatus.CLOSED, "CLOSE", actor, null);
    }

    @Transactional
    public MarketResponse startResolving(UUID id, String actor) {
        return transition(id, MarketStatus.RESOLVING, "START_RESOLVING", actor, null);
    }

    @Transactional
    public MarketResponse resolveMarket(UUID id, ResolveMarketRequest request) {
        MarketEntity market = findMarketOrThrow(id);
        MarketStatus from = market.getStatus();
        stateMachine.validateTransition(from, MarketStatus.RESOLVED);

        List<MarketOutcomeEntity> outcomes = outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(id);
        boolean winningExists = outcomes.stream()
                .anyMatch(o -> o.getOutcomeCode().equalsIgnoreCase(request.getWinningOutcomeCode()));
        if (!winningExists) {
            throw new BusinessException(
                    ErrorCode.OUTCOME_RULE_VIOLATION,
                    "Winning outcome code not found: " + request.getWinningOutcomeCode());
        }
        for (MarketOutcomeEntity outcome : outcomes) {
            boolean winning = outcome.getOutcomeCode().equalsIgnoreCase(request.getWinningOutcomeCode());
            outcome.setIsWinning(winning);
            if (winning) {
                outcome.setPayoutNumerator(
                        request.getPayoutNumerator() != null ? request.getPayoutNumerator() : BigDecimal.ONE);
                outcome.setPayoutDenominator(
                        request.getPayoutDenominator() != null ? request.getPayoutDenominator() : BigDecimal.ONE);
            } else {
                outcome.setIsWinning(false);
                outcome.setPayoutNumerator(BigDecimal.ZERO);
                outcome.setPayoutDenominator(BigDecimal.ONE);
            }
        }
        outcomeRepository.saveAll(outcomes);
        validationService.validateSingleWinningOutcome(outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(id));

        market.setStatus(MarketStatus.RESOLVED);
        MarketEntity saved = marketRepository.save(market);
        auditService.logTransition(id, from, MarketStatus.RESOLVED, "RESOLVE", request.getActor(), request.getWinningOutcomeCode());
        return mapper.toMarketResponse(saved);
    }

    @Transactional
    public MarketResponse settleMarket(UUID id, String actor) {
        MarketEntity market = findMarketOrThrow(id);
        List<MarketOutcomeEntity> outcomes = outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(id);
        validationService.validateSingleWinningOutcome(outcomes);
        return transition(id, MarketStatus.SETTLED, "SETTLE", actor, null);
    }

    @Transactional
    public MarketResponse cancelMarket(UUID id, CancelMarketRequest request) {
        MarketEntity market = findMarketOrThrow(id);
        if (market.getStatus() != MarketStatus.OPEN) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE_TRANSITION,
                    "Only OPEN markets can be cancelled");
        }
        if (Boolean.FALSE.equals(request.getForce()) && OffsetDateTime.now().isAfter(market.getOpenTime())) {
            throw new BusinessException(
                    ErrorCode.MARKET_RULE_VIOLATION,
                    "Cannot cancel OPEN market after open_time without force=true");
        }
        return transition(id, MarketStatus.CANCELLED, "CANCEL", request.getActor(), request.getReason());
    }

    public MarketEntity findMarketOrThrow(UUID id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Market not found: " + id));
    }

    private MarketResponse transition(UUID id, MarketStatus to, String action, String actor, Runnable preCheck) {
        MarketEntity market = findMarketOrThrow(id);
        MarketStatus from = market.getStatus();
        stateMachine.validateTransition(from, to);
        if (preCheck != null) {
            preCheck.run();
        }
        market.setStatus(to);
        MarketEntity saved = marketRepository.save(market);
        auditService.logTransition(id, from, to, action, actor != null ? actor : "system", null);
        return mapper.toMarketResponse(saved);
    }
}
