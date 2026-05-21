package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.SettlementEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<SettlementEntity, UUID> {

    List<SettlementEntity> findByMarketIdOrderBySettledAtDesc(UUID marketId);
}
