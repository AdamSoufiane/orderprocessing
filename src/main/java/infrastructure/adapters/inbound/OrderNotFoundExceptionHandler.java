package infrastructure.adapters.inbound;

import domain.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller advice to handle exceptions globally.
 */
@ControllerAdvice
public class OrderNotFoundExceptionHandler {

    /**
     * Handles the OrderNotFoundException when an order with the specified ID does not exist.
     * It constructs a detailed error response including the exception message and relevant HTTP status code.
     *
     * @param ex the OrderNotFoundException
     * @param request the WebRequest containing context information
     * @return a ResponseEntity containing the error attributes and HTTP status code
     */
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}