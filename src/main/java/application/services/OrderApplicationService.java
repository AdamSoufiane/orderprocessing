package application.services;

import application.ports.in.OrderProcessingPort;
import application.ports.out.OrderPersistencePort;
import domain.entities.Order;
import domain.exceptions.OrderNotFoundException;
import domain.vo.OrderRequest;
import domain.vo.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

@Slf4j
@AllArgsConstructor
public class OrderApplicationService {

    private final OrderProcessingPort orderProcessingPort;
    private final OrderPersistencePort orderPersistencePort;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {
            Order order = orderProcessingPort.processOrder(orderRequest);
            order = orderPersistencePort.saveOrder(order);
            return new OrderResponse(order);
        } catch (DataAccessException e) {
            log.error("Data access exception occurred while creating order: ", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while creating order: ", e);
            throw e;
        }
    }

    public Order retrieveOrder(Long orderId) {
        try {
            return orderPersistencePort.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Failed to retrieve order with id: " + orderId + ". Order not found."));
        } catch (DataAccessException e) {
            log.error("Data access exception occurred while retrieving order with id: {}", orderId, e);
            throw e;
        } catch (OrderNotFoundException e) {
            log.error("OrderNotFoundException occurred while retrieving order with id: {}", orderId, e);
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while retrieving order with id: {}", orderId, e);
            throw e;
        }
    }
}