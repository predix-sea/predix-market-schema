package com.predix.marketschema.controller;

import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.dto.request.CancelMarketRequest;
import com.predix.marketschema.dto.request.CreateMarketRequest;
import com.predix.marketschema.dto.request.LifecycleActionRequest;
import com.predix.marketschema.dto.request.ResolveMarketRequest;
import com.predix.marketschema.dto.request.UpdateMarketRequest;
import com.predix.marketschema.dto.response.ApiResponse;
import com.predix.marketschema.dto.response.MarketResponse;
import com.predix.marketschema.dto.response.PageResponse;
import com.predix.marketschema.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/markets")
@Tag(name = "Markets", description = "Market lifecycle and schema APIs")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create market in DRAFT status")
    public ApiResponse<MarketResponse> create(@Valid @RequestBody CreateMarketRequest request) {
        return ApiResponse.success(marketService.createMarket(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<MarketResponse> get(@PathVariable UUID id) {
        return ApiResponse.success(marketService.getMarket(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<MarketResponse>> list(
            @RequestParam(required = false) MarketStatus status,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(marketService.listMarkets(status, category, page, size));
    }

    @PatchMapping("/{id}")
    public ApiResponse<MarketResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateMarketRequest request) {
        return ApiResponse.success(marketService.updateMarket(id, request));
    }

    @PostMapping("/{id}/open")
    public ApiResponse<MarketResponse> open(@PathVariable UUID id, @RequestBody(required = false) LifecycleActionRequest request) {
        String actor = request != null ? request.getActor() : "system";
        return ApiResponse.success(marketService.openMarket(id, actor));
    }

    @PostMapping("/{id}/close")
    public ApiResponse<MarketResponse> close(@PathVariable UUID id, @RequestBody(required = false) LifecycleActionRequest request) {
        String actor = request != null ? request.getActor() : "system";
        return ApiResponse.success(marketService.closeMarket(id, actor));
    }

    @PostMapping("/{id}/start-resolving")
    public ApiResponse<MarketResponse> startResolving(
            @PathVariable UUID id,
            @RequestBody(required = false) LifecycleActionRequest request) {
        String actor = request != null ? request.getActor() : "system";
        return ApiResponse.success(marketService.startResolving(id, actor));
    }

    @PostMapping("/{id}/resolve")
    public ApiResponse<MarketResponse> resolve(@PathVariable UUID id, @Valid @RequestBody ResolveMarketRequest request) {
        return ApiResponse.success(marketService.resolveMarket(id, request));
    }

    @PostMapping("/{id}/settle")
    public ApiResponse<MarketResponse> settle(@PathVariable UUID id, @RequestBody(required = false) LifecycleActionRequest request) {
        String actor = request != null ? request.getActor() : "system";
        return ApiResponse.success(marketService.settleMarket(id, actor));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<MarketResponse> cancel(@PathVariable UUID id, @Valid @RequestBody CancelMarketRequest request) {
        return ApiResponse.success(marketService.cancelMarket(id, request));
    }
}
