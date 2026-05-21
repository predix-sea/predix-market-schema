package com.predix.marketschema.controller;

import com.predix.marketschema.dto.request.CreateResolutionRecordRequest;
import com.predix.marketschema.dto.response.ApiResponse;
import com.predix.marketschema.dto.response.ResolutionRecordResponse;
import com.predix.marketschema.service.ResolutionService;
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
@RequestMapping("/api/v1/markets/{marketId}/resolution-records")
@Tag(name = "Resolution")
public class ResolutionController {

    private final ResolutionService resolutionService;

    public ResolutionController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ResolutionRecordResponse> create(
            @PathVariable UUID marketId,
            @Valid @RequestBody CreateResolutionRecordRequest request) {
        return ApiResponse.success(resolutionService.createResolution(marketId, request));
    }

    @GetMapping
    public ApiResponse<List<ResolutionRecordResponse>> list(@PathVariable UUID marketId) {
        return ApiResponse.success(resolutionService.listResolutions(marketId));
    }
}
