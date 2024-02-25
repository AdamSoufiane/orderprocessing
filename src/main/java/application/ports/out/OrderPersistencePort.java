package application.ports.out;

import domain.entities.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPersistencePort {

    Order saveOrder(Order order) throws DataAccessException;

    Optional<Order> findById(Long id) throws DataAccessException;

}