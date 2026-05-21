package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MarketValidationService {

    private static final Set<String> BINARY_OUTCOMES = Set.of("YES", "NO");

    public void validateTimeWindow(OffsetDateTime open, OffsetDateTime close, OffsetDateTime resolveDeadline) {
        if (open == null || close == null || resolveDeadline == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Time window fields are required");
        }
        if (!open.isBefore(close) || !close.isBefore(resolveDeadline)) {
            throw new BusinessException(
                    ErrorCode.MARKET_RULE_VIOLATION,
                    "Time window must satisfy: open_time < close_time < resolve_deadline");
        }
    }

    public void validateOutcomesForOpen(MarketEntity market, List<MarketOutcomeEntity> outcomes) {
        if (outcomes.size() < 2) {
            throw new BusinessException(ErrorCode.OUTCOME_RULE_VIOLATION, "Market must have at least 2 outcomes");
        }
        if (market.getMarketType() == MarketType.BINARY) {
            Set<String> codes = outcomes.stream()
                    .map(o -> o.getOutcomeCode().toUpperCase())
                    .collect(Collectors.toSet());
            if (outcomes.size() != 2 || !codes.equals(BINARY_OUTCOMES)) {
                throw new BusinessException(
                        ErrorCode.OUTCOME_RULE_VIOLATION,
                        "Binary market must have exactly YES and NO outcomes");
            }
        }
        validateContinuousIndices(outcomes);
    }

    public void validateContinuousIndices(List<MarketOutcomeEntity> outcomes) {
        List<Integer> indices = outcomes.stream()
                .map(MarketOutcomeEntity::getOutcomeIndex)
                .sorted()
                .toList();
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) != i) {
                throw new BusinessException(
                        ErrorCode.OUTCOME_RULE_VIOLATION,
                        "Outcome indices must be continuous from 0 to N-1");
            }
        }
    }

    public void validateSingleWinningOutcome(List<MarketOutcomeEntity> outcomes) {
        long winningCount = outcomes.stream().filter(o -> Boolean.TRUE.equals(o.getIsWinning())).count();
        if (winningCount != 1) {
            throw new BusinessException(
                    ErrorCode.MARKET_RULE_VIOLATION,
                    "Exactly one winning outcome is required when resolving");
        }
    }

    public void assertNotSettled(MarketEntity market) {
        if (market.getStatus() == MarketStatus.SETTLED) {
            throw new BusinessException(
                    ErrorCode.IMMUTABLE_MARKET,
                    "Market is SETTLED and cannot be modified");
        }
    }

    public void validateOutcomeModificationAllowed(MarketEntity market) {
        if (market.getStatus() == MarketStatus.SETTLED || market.getStatus() == MarketStatus.RESOLVED) {
            throw new BusinessException(
                    ErrorCode.IMMUTABLE_MARKET,
                    "Cannot modify outcomes when market is RESOLVED or SETTLED");
        }
    }

    public int nextOutcomeIndex(List<MarketOutcomeEntity> existing) {
        return existing.stream()
                .map(MarketOutcomeEntity::getOutcomeIndex)
                .max(Comparator.naturalOrder())
                .orElse(-1) + 1;
    }
}
