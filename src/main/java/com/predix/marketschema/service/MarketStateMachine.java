package com.predix.marketschema.service;

import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MarketStateMachine {

    private static final Map<MarketStatus, Set<MarketStatus>> TRANSITIONS = new EnumMap<>(MarketStatus.class);

    static {
        TRANSITIONS.put(MarketStatus.DRAFT, EnumSet.of(MarketStatus.OPEN));
        TRANSITIONS.put(MarketStatus.OPEN, EnumSet.of(MarketStatus.CLOSED, MarketStatus.CANCELLED));
        TRANSITIONS.put(MarketStatus.CLOSED, EnumSet.of(MarketStatus.RESOLVING));
        TRANSITIONS.put(MarketStatus.RESOLVING, EnumSet.of(MarketStatus.RESOLVED));
        TRANSITIONS.put(MarketStatus.RESOLVED, EnumSet.of(MarketStatus.SETTLED));
        TRANSITIONS.put(MarketStatus.SETTLED, EnumSet.noneOf(MarketStatus.class));
        TRANSITIONS.put(MarketStatus.CANCELLED, EnumSet.noneOf(MarketStatus.class));
    }

    public void validateTransition(MarketStatus from, MarketStatus to) {
        Set<MarketStatus> allowed = TRANSITIONS.getOrDefault(from, EnumSet.noneOf(MarketStatus.class));
        if (!allowed.contains(to)) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE_TRANSITION,
                    "Cannot transition market from " + from + " to " + to);
        }
    }

    public boolean canTransition(MarketStatus from, MarketStatus to) {
        return TRANSITIONS.getOrDefault(from, EnumSet.noneOf(MarketStatus.class)).contains(to);
    }
}
