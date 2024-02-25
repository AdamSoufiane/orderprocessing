package domain.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class OrderItem implements Comparable<OrderItem> {

    private final String productId;
    @Positive
    private final int quantity;
    @Positive
    private final BigDecimal unitPrice;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    @Override
    public int compareTo(OrderItem other) {
        return Comparator.comparing(OrderItem::getProductId)
                .thenComparingInt(OrderItem::getQuantity)
                .thenComparing(OrderItem::getUnitPrice)
                .compare(this, other);
    }

    public OrderItem withUpdatedQuantity(int newQuantity) {
        return new OrderItem(this.productId, newQuantity, this.unitPrice);
    }
}