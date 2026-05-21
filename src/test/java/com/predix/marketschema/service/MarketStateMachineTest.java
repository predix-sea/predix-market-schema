package com.predix.marketschema.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import org.junit.jupiter.api.Test;

class MarketStateMachineTest {

    private final MarketStateMachine stateMachine = new MarketStateMachine();

    @Test
    void allowsValidTransitions() {
        assertThat(stateMachine.canTransition(MarketStatus.DRAFT, MarketStatus.OPEN)).isTrue();
        assertThat(stateMachine.canTransition(MarketStatus.OPEN, MarketStatus.CLOSED)).isTrue();
        assertThat(stateMachine.canTransition(MarketStatus.OPEN, MarketStatus.CANCELLED)).isTrue();
        assertThat(stateMachine.canTransition(MarketStatus.CLOSED, MarketStatus.RESOLVING)).isTrue();
        assertThat(stateMachine.canTransition(MarketStatus.RESOLVING, MarketStatus.RESOLVED)).isTrue();
        assertThat(stateMachine.canTransition(MarketStatus.RESOLVED, MarketStatus.SETTLED)).isTrue();
    }

    @Test
    void rejectsInvalidTransitions() {
        assertThat(stateMachine.canTransition(MarketStatus.DRAFT, MarketStatus.CLOSED)).isFalse();
        assertThat(stateMachine.canTransition(MarketStatus.SETTLED, MarketStatus.OPEN)).isFalse();
        assertThat(stateMachine.canTransition(MarketStatus.CANCELLED, MarketStatus.OPEN)).isFalse();

        assertThatThrownBy(() -> stateMachine.validateTransition(MarketStatus.DRAFT, MarketStatus.RESOLVED))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_STATE_TRANSITION);
    }
}
