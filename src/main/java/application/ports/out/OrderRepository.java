package application.ports.out;

import domain.entities.Order;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    Order saveOrder(Order order);

    Optional<Order> findById(Long id);
}