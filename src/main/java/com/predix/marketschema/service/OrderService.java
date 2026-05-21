package com.predix.marketschema.service;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.entity.MarketOutcomeEntity;
import com.predix.marketschema.domain.entity.OrderEntity;
import com.predix.marketschema.domain.entity.PositionEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import com.predix.marketschema.domain.enums.OrderStatus;
import com.predix.marketschema.dto.request.CreateOrderRequest;
import com.predix.marketschema.dto.response.OrderResponse;
import com.predix.marketschema.dto.response.PageResponse;
import com.predix.marketschema.exception.BusinessException;
import com.predix.marketschema.exception.ErrorCode;
import com.predix.marketschema.repository.MarketOutcomeRepository;
import com.predix.marketschema.repository.OrderRepository;
import com.predix.marketschema.repository.PositionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MarketOutcomeRepository outcomeRepository;
    private final PositionRepository positionRepository;
    private final MarketService marketService;
    private final EntityMapper mapper;

    public OrderService(
            OrderRepository orderRepository,
            MarketOutcomeRepository outcomeRepository,
            PositionRepository positionRepository,
            MarketService marketService,
            EntityMapper mapper) {
        this.orderRepository = orderRepository;
        this.outcomeRepository = outcomeRepository;
        this.positionRepository = positionRepository;
        this.marketService = marketService;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        MarketEntity market = marketService.findMarketOrThrow(request.getMarketId());
        if (market.getStatus() != MarketStatus.OPEN) {
            throw new BusinessException(ErrorCode.ORDER_RULE_VIOLATION, "Orders can only be placed on OPEN markets");
        }

        MarketOutcomeEntity outcome = outcomeRepository.findById(request.getOutcomeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Outcome not found"));
        if (!outcome.getMarketId().equals(request.getMarketId())) {
            throw new BusinessException(ErrorCode.ORDER_RULE_VIOLATION, "Outcome does not belong to market");
        }

        OrderEntity order = new OrderEntity();
        order.setOrderCode(generateOrderCode());
        order.setMarketId(request.getMarketId());
        order.setOutcomeId(request.getOutcomeId());
        order.setSide(request.getSide());
        order.setOrderType(request.getOrderType());
        order.setPrice(request.getPrice());
        order.setQuantity(request.getQuantity());
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setUserId(request.getUserId());
        order.setStatus(OrderStatus.NEW);

        OrderEntity saved = orderRepository.save(order);
        upsertPosition(saved);
        return mapper.toOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        return mapper.toOrderResponse(findOrderOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> listOrders(UUID marketId, String userId, OrderStatus status, int page, int size) {
        Page<OrderEntity> result = orderRepository.findByFilters(marketId, userId, status, PageRequest.of(page, size));
        List<OrderResponse> content = result.getContent().stream().map(mapper::toOrderResponse).toList();
        return mapper.toPageResponse(result, content);
    }

    private void upsertPosition(OrderEntity order) {
        PositionEntity position = positionRepository
                .findByFilters(order.getMarketId(), order.getUserId())
                .stream()
                .filter(p -> p.getOutcomeId().equals(order.getOutcomeId()))
                .findFirst()
                .orElseGet(() -> {
                    PositionEntity p = new PositionEntity();
                    p.setMarketId(order.getMarketId());
                    p.setOutcomeId(order.getOutcomeId());
                    p.setUserId(order.getUserId());
                    p.setQuantity(BigDecimal.ZERO);
                    p.setAvgCost(BigDecimal.ZERO);
                    return p;
                });

        BigDecimal qty = order.getQuantity();
        BigDecimal newQty = position.getQuantity().add(qty);
        if (newQty.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalCost = position.getAvgCost().multiply(position.getQuantity()).add(order.getPrice().multiply(qty));
            position.setAvgCost(totalCost.divide(newQty, 18, RoundingMode.HALF_UP));
        }
        position.setQuantity(newQty);
        positionRepository.save(position);
    }

    private OrderEntity findOrderOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Order not found: " + id));
    }

    private String generateOrderCode() {
        return "ORD_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
