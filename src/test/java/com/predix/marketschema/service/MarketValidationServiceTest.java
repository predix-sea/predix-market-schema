package com.predix.marketschema.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.MarketType;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketValidationServiceTest {

    private MarketValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new MarketValidationService();
    }

    @Test
    void rejectsInvalidTimeWindow() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        assertThatThrownBy(() -> validationService.validateTimeWindow(now, now.minusHours(1), now.plusDays(1)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.MARKET_RULE_VIOLATION);
    }

    @Test
    void binaryMarketRequiresYesNo() {
        MarketEntity market = new MarketEntity();
        market.setMarketType(MarketType.BINARY);

        MarketOutcomeEntity yes = outcome("YES", 0);
        MarketOutcomeEntity maybe = outcome("MAYBE", 1);

        assertThatThrownBy(() -> validationService.validateOutcomesForOpen(market, List.of(yes, maybe)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.OUTCOME_RULE_VIOLATION);
    }

    @Test
    void requiresExactlyOneWinningOutcome() {
        MarketOutcomeEntity yes = outcome("YES", 0);
        yes.setIsWinning(true);
        MarketOutcomeEntity no = outcome("NO", 1);
        no.setIsWinning(true);

        assertThatThrownBy(() -> validationService.validateSingleWinningOutcome(List.of(yes, no)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.MARKET_RULE_VIOLATION);
    }

    @Test
    void requiresContinuousOutcomeIndices() {
        MarketOutcomeEntity o0 = outcome("YES", 0);
        MarketOutcomeEntity o2 = outcome("NO", 2);

        assertThatThrownBy(() -> validationService.validateContinuousIndices(List.of(o0, o2)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.OUTCOME_RULE_VIOLATION);
    }

    @Test
    void settledMarketIsImmutable() {
        MarketEntity market = new MarketEntity();
        market.setStatus(MarketStatus.SETTLED);

        assertThatThrownBy(() -> validationService.assertNotSettled(market))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.IMMUTABLE_MARKET);
    }

    private MarketOutcomeEntity outcome(String code, int index) {
        MarketOutcomeEntity o = new MarketOutcomeEntity();
        o.setId(UUID.randomUUID());
        o.setOutcomeCode(code);
        o.setOutcomeIndex(index);
        o.setOutcomeLabel(code);
        return o;
    }
}
