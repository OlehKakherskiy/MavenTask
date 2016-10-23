package ua.rd.pizzaservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import ua.rd.pizzaservice.domain.*;
import ua.rd.pizzaservice.infrastructure.Benchmark;
import ua.rd.pizzaservice.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public class SimpleOrderService implements OrderService {

    private OrderRepository orderRepository;

    private PizzaService pizzaService;

    private List<AbstractDiscount> availableDiscounts;

    private int maxPizzaCount;

    @Autowired
    public SimpleOrderService(OrderRepository orderRepository, PizzaService pizzaService,
                              int maxPizzaCount, List<AbstractDiscount> availableDiscounts) {
        this.orderRepository = orderRepository;
        this.pizzaService = pizzaService;
        this.maxPizzaCount = maxPizzaCount;
        this.availableDiscounts = availableDiscounts;
    }

    @Override
//    @Benchmark(switchedOn = true)
    public Order placeNewOrder(Customer customer, Map<Integer, Integer> pizzasCount) {
        verifyPizzasCount(new ArrayList<>(pizzasCount.values()));
        Order newOrder = createOrder(customer, findAllPizzas(pizzasCount));
        addOrderPriceToCustomersCard(customer, newOrder.calculateTotalPrice()); //total price without discounts
        applyDiscounts(newOrder);
        saveOrder(newOrder);
        return newOrder;
    }

    @StateTransitions({
            @StateTransition(from = OrderState.IN_PROGRESS, to = OrderState.CANCELLED),
            @StateTransition(from = OrderState.CANCELLED, to = OrderState.CANCELLED)
    })
    @Override
    public void closeOrder(Long orderId) {
    }

    @StateTransitions({
            @StateTransition(from = OrderState.IN_PROGRESS, to = OrderState.IN_PROGRESS),
            @StateTransition(from = OrderState.DONE, to = OrderState.IN_PROGRESS)
    })
    @Override
    public void addPizza(Long orderId, Integer pizzaId, int count) {
        Order order = orderRepository.readOrder(orderId);
        Pizza pizza = pizzaService.find(pizzaId);
        if (order == null || pizza == null) {
            throw new IllegalArgumentException();//TODO:
        }

    }

    @StateTransitions({
            @StateTransition(from = OrderState.IN_PROGRESS, to = OrderState.IN_PROGRESS)
    })
    @Override
    public void removePizza(Long orderId, Integer pizzaId, int count) {
        Order order = orderRepository.readOrder(orderId);
        Pizza pizza = pizzaService.find(pizzaId);
        if (order == null || pizza == null) {
            throw new IllegalArgumentException();//TODO:
        }
        order.removePizza(pizza, count);
    }

    @StateTransitions({
            @StateTransition(from = OrderState.DONE, to = OrderState.CANCELLED)
    })
    @Override
    public void payForOrder(Long orderId, double price) {
        closeOrder(orderId);
    }

    @Override
    public double getOrderPrice(Long orderId) {
        Order order = orderRepository.readOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("There's no order with id = " + orderId);
        }
        return getOrderPrice(order);
    }

    private double getOrderPrice(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order can't be null");
        }
        return order.calculateTotalPrice();
    }

    private void addOrderPriceToCustomersCard(Customer customer, double orderPrice) {
        CustomerCard customerCard = customer.getCustomerCard();
        if (customerCard != null) {
            customerCard.addOrderPrice(orderPrice);
        }
    }

    private void verifyPizzasCount(List<Integer> pizzasCount) {
        int sum = getPizzaSumInOrder(pizzasCount);
        if (sum > maxPizzaCount) {
            throw new IllegalArgumentException("pizzas count in order is bigger then max possible amount. Max = "
                    + maxPizzaCount + ", but were " + sum);
        }
    }

    private int getPizzaSumInOrder(List<Integer> counts) {
        int sum = 0;
        for (Integer cnt : counts) {
            sum += cnt;
        }
        return sum;
    }

    private Order createOrder(Customer customer, Map<Pizza, Integer> pizzas) {
        return new Order(customer, pizzas, new ArrayList<>());
    }

    private Map<Pizza, Integer> findAllPizzas(Map<Integer, Integer> pizzasCount) {
        Map<Pizza, Integer> pizzasAndCounts = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : pizzasCount.entrySet()) {
            pizzasAndCounts.put(findPizzaById(entry.getKey()), entry.getValue());
        }

        return pizzasAndCounts;
    }

    private void applyDiscounts(Order order) {
        for (AbstractDiscount discount : availableDiscounts) {
            discount.applyDiscount(order);
        }
    }

    private Pizza findPizzaById(Integer id) {
        return pizzaService.find(id);
    }

    private void saveOrder(Order newOrder) {
        orderRepository.saveOrder(newOrder);
    }
}
