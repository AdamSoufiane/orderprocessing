package infrastructure.adapters.inbound;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import domain.vo.OrderItem;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "Customer ID must not be null")
    private String customerId;

    @NotNull(message = "Items list must not be null")
    @Size(min = 1, message = "Items list must not be empty")
    private List<@NotNull(message = "Item must not be null") OrderItem> items;

}