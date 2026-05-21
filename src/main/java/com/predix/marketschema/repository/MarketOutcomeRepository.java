package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketOutcomeRepository extends JpaRepository<MarketOutcomeEntity, UUID> {

    List<MarketOutcomeEntity> findByMarketIdOrderByOutcomeIndexAsc(UUID marketId);

    Optional<MarketOutcomeEntity> findByMarketIdAndOutcomeCode(UUID marketId, String outcomeCode);

    long countByMarketId(UUID marketId);
}
