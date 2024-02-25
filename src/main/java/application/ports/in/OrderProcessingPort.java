package application.ports.in;

import application.ports.in.OrderRequest;
import application.ports.in.OrderResponse;
import domain.exceptions.OrderNotFoundException;

/**
 * Defines the contract for order processing operations.
 */
public interface OrderProcessingPort {

    /**
     * This method takes an OrderRequest object as input and processes it to create an order.
     * It ensures all business rules are applied and the order is valid.
     * If successful, it returns an OrderResponse object with the details of the processed order.
     * If the order cannot be processed, an OrderNotFoundException is thrown.
     *
     * @param orderRequest the order request to process
     * @return the order response after processing
     * @throws OrderNotFoundException if the order cannot be found or processed
     */
    OrderResponse processOrder(OrderRequest orderRequest) throws OrderNotFoundException;

}