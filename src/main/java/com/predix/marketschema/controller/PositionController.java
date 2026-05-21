package com.predix.marketschema.controller;

import com.predix.marketschema.dto.response.ApiResponse;
import com.predix.marketschema.dto.response.PositionResponse;
import com.predix.marketschema.service.PositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/positions")
@Tag(name = "Positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ApiResponse<List<PositionResponse>> list(
            @RequestParam(required = false) UUID marketId,
            @RequestParam(required = false) String userId) {
        return ApiResponse.success(positionService.listPositions(marketId, userId));
    }
}
