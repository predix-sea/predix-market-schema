package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketAuditLogEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.repository.MarketAuditLogRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketAuditService {

    private final MarketAuditLogRepository auditLogRepository;

    public MarketAuditService(MarketAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logTransition(UUID marketId, MarketStatus from, MarketStatus to, String action, String actor, String detail) {
        MarketAuditLogEntity log = new MarketAuditLogEntity();
        log.setMarketId(marketId);
        log.setFromStatus(from);
        log.setToStatus(to);
        log.setAction(action);
        log.setActor(actor);
        log.setDetail(detail);
        auditLogRepository.save(log);
    }
}
