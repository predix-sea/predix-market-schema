package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.ResolutionRecordEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.dto.request.CreateResolutionRecordRequest;
import com.predix.marketschema.dto.response.ResolutionRecordResponse;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.ResolutionRecordRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResolutionService {

    private final ResolutionRecordRepository resolutionRepository;
    private final MarketService marketService;
    private final EntityMapper mapper;

    public ResolutionService(
            ResolutionRecordRepository resolutionRepository,
            MarketService marketService,
            EntityMapper mapper) {
        this.resolutionRepository = resolutionRepository;
        this.marketService = marketService;
        this.mapper = mapper;
    }

    @Transactional
    public ResolutionRecordResponse createResolution(UUID marketId, CreateResolutionRecordRequest request) {
        MarketEntity market = marketService.findMarketOrThrow(marketId);
        if (market.getStatus() != MarketStatus.RESOLVING && market.getStatus() != MarketStatus.RESOLVED) {
            throw new BusinessException(
                    ErrorCode.MARKET_RULE_VIOLATION,
                    "Resolution records require market in RESOLVING or RESOLVED status");
        }

        ResolutionRecordEntity record = new ResolutionRecordEntity();
        record.setMarketId(marketId);
        record.setResolutionSource(request.getResolutionSource());
        record.setUmaRequestTxHash(request.getUmaRequestTxHash());
        record.setUmaAssertionId(request.getUmaAssertionId());
        record.setProposedOutcomeCode(request.getProposedOutcomeCode());
        record.setDisputed(request.getDisputed() != null ? request.getDisputed() : false);
        record.setFinalOutcomeCode(request.getFinalOutcomeCode());
        record.setResolvedAt(request.getResolvedAt() != null ? request.getResolvedAt() : OffsetDateTime.now());
        record.setResolverRef(request.getResolverRef());
        record.setRawPayload(request.getRawPayload());

        return mapper.toResolutionResponse(resolutionRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<ResolutionRecordResponse> listResolutions(UUID marketId) {
        marketService.findMarketOrThrow(marketId);
        return resolutionRepository.findByMarketIdOrderByCreatedAtDesc(marketId).stream()
                .map(mapper::toResolutionResponse)
                .toList();
    }
}
