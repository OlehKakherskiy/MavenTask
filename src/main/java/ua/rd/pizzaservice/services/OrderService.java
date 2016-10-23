package ua.rd.pizzaservice.services;

import ua.rd.pizzaservice.domain.*;

import java.util.Map;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public interface OrderService {

    Order placeNewOrder(Customer customer, Map<Integer, Integer> pizzasCount);

    void closeOrder(Long orderId);

    void addPizza(Long orderId, Integer pizzaId, int count);

    void removePizza(Long orderId, Integer pizzaId, int count);

    void payForOrder(Long orderId, double price);

    double getOrderPrice(Long id);
}
