package com.predix.marketschema.controller;

import com.predix.marketschema.dto.request.CreateSettlementRequest;
import com.predix.marketschema.dto.response.ApiResponse;
import com.predix.marketschema.dto.response.SettlementResponse;
import com.predix.marketschema.service.SettlementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/markets/{marketId}/settlements")
@Tag(name = "Settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SettlementResponse> create(
            @PathVariable UUID marketId,
            @Valid @RequestBody CreateSettlementRequest request) {
        return ApiResponse.success(settlementService.createSettlement(marketId, request));
    }

    @GetMapping
    public ApiResponse<List<SettlementResponse>> list(@PathVariable UUID marketId) {
        return ApiResponse.success(settlementService.listSettlements(marketId));
    }
}
