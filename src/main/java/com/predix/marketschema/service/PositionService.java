package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.PositionEntity;
import com.predix.marketschema.dto.response.PositionResponse;
import com.predix.marketschema.repository.PositionRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final EntityMapper mapper;

    public PositionService(PositionRepository positionRepository, EntityMapper mapper) {
        this.positionRepository = positionRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<PositionResponse> listPositions(UUID marketId, String userId) {
        return positionRepository.findByFilters(marketId, userId).stream()
                .map(mapper::toPositionResponse)
                .toList();
    }
}
