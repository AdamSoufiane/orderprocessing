package domain.exceptions;

public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super("Order with ID " + orderId + " not found");
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
