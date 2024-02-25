package infrastructure.adapters.inbound;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    @NotNull
    private Long id;

    @NotNull
    private String status;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalPrice;

    @NotNull
    private LocalDateTime createdDate;
}