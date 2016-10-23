package ua.rd.pizzaservice.repository;

import ua.rd.pizzaservice.domain.Order;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public interface OrderRepository {

    void saveOrder(Order order);

    Order readOrder(Long id);

}
