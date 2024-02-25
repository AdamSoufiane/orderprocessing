package application.services;

import application.ports.in.OrderProcessingPort;
import application.ports.out.OrderPersistencePort;
import domain.entities.Order;
import domain.exceptions.OrderNotFoundException;
import infrastructure.adapters.inbound.OrderRequest;
import infrastructure.adapters.inbound.OrderResponse;
import infrastructure.common.OrderMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OrderProcessingService implements OrderProcessingPort {

    private final OrderPersistencePort orderPersistencePort;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse processOrder(OrderRequest orderRequest) throws OrderNotFoundException {
        if (orderRequest == null || orderRequest.getCustomerId() == null || orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            log.error("Invalid order request");
            throw new IllegalArgumentException("Invalid order request");
        }
        try {
            Order order = orderMapper.toOrderEntity(orderRequest);
            Order savedOrder = orderPersistencePort.saveOrder(order);
            log.info("Order with ID {} has been successfully persisted.", savedOrder.getId());
            return orderMapper.toOrderResponse(savedOrder);
        } catch (DataAccessException e) {
            log.error("Failed to save order: ", e);
            throw e;
        }
    }
}