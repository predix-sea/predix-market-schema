package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.MarketAuditLogEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketAuditLogRepository extends JpaRepository<MarketAuditLogEntity, UUID> {

    List<MarketAuditLogEntity> findByMarketIdOrderByCreatedAtDesc(UUID marketId);
}
