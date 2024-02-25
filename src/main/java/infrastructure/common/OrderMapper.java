package infrastructure.common;

import domain.entities.CustomerDetails;
import domain.entities.MonetaryAmount;
import domain.entities.Order;
import domain.vo.OrderItem;
import domain.vo.OrderStatus;
import domain.exceptions.OrderNotFoundException;
import infrastructure.adapters.inbound.OrderRequest;
import infrastructure.adapters.inbound.OrderResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class OrderMapper {

    @Getter @Setter
    private String mapperVersion = "1.0.0";

    public Order toOrderEntity(OrderRequest orderRequest) {
        if (orderRequest == null) {
            log.error("OrderRequest is null");
            throw new IllegalArgumentException("OrderRequest must not be null");
        }
        log.info("Mapping OrderRequest to Order entity.");
        CustomerDetails customerDetails = new CustomerDetails(orderRequest.getCustomerId());
        if (customerDetails == null) {
            log.error("CustomerDetails is null");
            throw new IllegalArgumentException("CustomerDetails must not be null");
        }
        List<OrderItem> items = orderRequest.getItems().stream()
                .map(requestItem -> {
                    if (requestItem.getQuantity() <= 0 || requestItem.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("Quantity and unit price must be positive");
                    }
                    return new OrderItem(requestItem.getProductId(), requestItem.getQuantity(), new MonetaryAmount(requestItem.getUnitPrice()));
                })
                .collect(Collectors.toList());
        BigDecimal tax = order.calculateTax(); // Placeholder for tax calculation logic
        MonetaryAmount totalPrice = new MonetaryAmount(items.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add).add(tax));
        Order order = new Order(UUID.randomUUID().toString(), totalPrice, OrderStatus.PENDING, LocalDateTime.now(ZoneId.of("UTC")), customerDetails, items, "System", LocalDateTime.now(ZoneId.of("UTC")));
        return order;
    }

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            log.error("Order is null");
            throw new OrderNotFoundException("Order must not be null");
        }
        log.info("Mapping Order to OrderResponse DTO.");
        MonetaryAmount totalPrice = order.getTotalPrice();
        if (totalPrice == null) {
            throw new OrderNotFoundException("Order's total price is null");
        }
        String currency = totalPrice.getCurrency();
        BigDecimal amount = totalPrice.getAmount();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId() != null ? order.getId() : UUID.randomUUID().toString()); // Generate new ID if not present
        orderResponse.setStatus(order.getStatus() != null ? order.getStatus().toString() : "UNKNOWN");
        orderResponse.setTotalPrice(amount);
        orderResponse.setCurrency(currency);
        orderResponse.setCreatedDate(order.getOrderDate());
        return orderResponse;
    }

}