package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.ResolutionRecordEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResolutionRecordRepository extends JpaRepository<ResolutionRecordEntity, UUID> {

    List<ResolutionRecordEntity> findByMarketIdOrderByCreatedAtDesc(UUID marketId);
}
