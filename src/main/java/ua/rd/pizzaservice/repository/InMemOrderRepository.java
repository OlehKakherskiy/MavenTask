package ua.rd.pizzaservice.repository;

import org.springframework.stereotype.Repository;
import ua.rd.pizzaservice.domain.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
@Repository
public class InMemOrderRepository implements OrderRepository {

    private static Map<Long, Order> orderList;

    private static long generatedId = 0;

    public InMemOrderRepository() {
        orderList = new HashMap<>();
    }

    @Override
    public void saveOrder(Order order) {
        long id = ++generatedId;
        orderList.put(id, order);
        order.setId(id);
    }

    @Override
    public Order readOrder(Long id) {
        return orderList.get(id);
    }
}
