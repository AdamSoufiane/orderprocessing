package infrastructure.adapters.outbound;

import application.ports.out.OrderPersistencePort;
import application.ports.out.OrderRepository;
import domain.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SQLOrderAdapter implements OrderPersistencePort {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order saveOrder(Order order) throws DataAccessException {
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
