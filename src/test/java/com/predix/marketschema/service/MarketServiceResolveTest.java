package com.predix.marketschema.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.dto.request.ResolveMarketRequest;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.MarketOutcomeRepository;
import com.predix.marketschema.repository.MarketRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketServiceResolveTest {

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private MarketOutcomeRepository outcomeRepository;

    @Mock
    private MarketStateMachine stateMachine;

    @Mock
    private MarketValidationService validationService;

    @Mock
    private MarketAuditService auditService;

    @Mock
    private MarketCodeGenerator codeGenerator;

    @Mock
    private EntityMapper mapper;

    @InjectMocks
    private MarketService marketService;

    @Test
    void resolve_rejectsUnknownWinningOutcome() {
        UUID marketId = UUID.randomUUID();
        MarketEntity market = new MarketEntity();
        market.setId(marketId);
        market.setStatus(MarketStatus.RESOLVING);

        MarketOutcomeEntity yes = new MarketOutcomeEntity();
        yes.setOutcomeCode("YES");

        when(marketRepository.findById(marketId)).thenReturn(Optional.of(market));
        when(outcomeRepository.findByMarketIdOrderByOutcomeIndexAsc(marketId)).thenReturn(List.of(yes));

        ResolveMarketRequest request = new ResolveMarketRequest();
        request.setWinningOutcomeCode("NO");
        request.setActor("oracle");

        assertThatThrownBy(() -> marketService.resolveMarket(marketId, request))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.OUTCOME_RULE_VIOLATION);
    }
}
