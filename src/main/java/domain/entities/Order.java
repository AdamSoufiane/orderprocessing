package domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import domain.vo.MonetaryAmountFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private CustomerDetails customerDetails;

    @NotNull
    private List<OrderItem> items;

    @NotNull
    private MonetaryAmount totalPrice;

    @NotNull
    private OrderStatus status;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private String createdBy;

    @NotNull
    private LocalDateTime lastUpdated;

    private MonetaryAmountFactory monetaryAmountFactory;

    public MonetaryAmount calculateTotalPrice() {
        return items.stream()
                     .map(OrderItem::getTotalPrice)
                     .reduce(MonetaryAmount::add)
                     .orElse(monetaryAmountFactory.createZeroAmount(totalPrice.getCurrency()));
    }

    public boolean addItem(OrderItem item) {
        validateCurrency(item.getTotalPrice());
        boolean added = items.add(item);
        if (added) {
            this.totalPrice = calculateTotalPrice();
            updateLastUpdated();
        }
        return added;
    }

    public boolean addItems(List<OrderItem> newItems) {
        newItems.forEach(this::validateCurrency);
        boolean added = items.addAll(newItems);
        if (added) {
            this.totalPrice = calculateTotalPrice();
            updateLastUpdated();
        }
        return added;
    }

    public MonetaryAmount calculateTax() {
        // Actual tax calculation logic
        double taxRate = monetaryAmountFactory.getCurrentTaxRate();
        return items.stream()
                     .filter(OrderItem::isTaxable)
                     .map(OrderItem::getTotalPrice)
                     .map(amount -> amount.multiply(taxRate))
                     .reduce(MonetaryAmount::add)
                     .orElse(monetaryAmountFactory.createZeroAmount(totalPrice.getCurrency()));
    }

    private boolean validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Actual status transition validation logic
        if (newStatus == null || !currentStatus.isValidTransition(newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
        return true;
    }

    private void validateCurrency(MonetaryAmount amount) {
        if (!amount.getCurrency().equals(totalPrice.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch: expected " + totalPrice.getCurrency() + " but got " + amount.getCurrency());
        }
    }

    private void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Other methods...
}
