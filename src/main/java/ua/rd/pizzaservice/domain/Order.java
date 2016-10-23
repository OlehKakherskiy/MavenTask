package ua.rd.pizzaservice.domain;

import java.util.*;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public class Order {

    private Long id;

    private Map<Pizza, Integer> pizzasCount;

    private Customer customer;

    private List<AbstractDiscount> discounts;

    private double orderPrice = -1;

    private OrderState currentState;

    public Order(Customer customer, Map<Pizza, Integer> pizzas, List<AbstractDiscount> discounts) {
        this.pizzasCount = pizzas;
        this.customer = customer;
        this.discounts = (discounts == null) ? new ArrayList<>() : discounts;
    }

    public void addDiscount(AbstractDiscount discount) {
        if (discount != null) {
            discounts.add(discount);
        }
    }

    public int getPizzaCount() {
        int sum = 0;
        for (Integer count : pizzasCount.values()) {
            sum += count;
        }
        return sum;
    }

    public double findMaxPizzaPrice() {
        return Collections.max(pizzasCount.keySet(), new Comparator<Pizza>() {
            @Override
            public int compare(Pizza o1, Pizza o2) {
                return (int) Math.ceil(o1.getPrice() - o2.getPrice());
            }
        }).getPrice();
    }

    public double calculateTotalPrice() {
        return calculateOrderPrice() - calculateDiscountPrice();
    }

    public double calculateOrderPrice() {
        if (orderPrice > 0) {
            return orderPrice;
        }
        double sum = 0;
        for (Map.Entry<Pizza, Integer> entry : pizzasCount.entrySet()) {
            sum += calculatePizzaPrice(entry.getKey(), entry.getValue());
        }
        orderPrice = sum;
        return sum;
    }

    public void changeState(OrderState orderState) {
        currentState = orderState;
    }

    public void addPizza(Pizza pizza, int count) {
        if (!isPositivePizzaCount(count))
            return;
        if (pizzasCount.containsKey(pizza)) {
            changePizzaCount(pizza, count);
        } else {
            pizzasCount.put(pizza, count);
        }
        invalidatePriceCache();
    }

    public void removePizza(Pizza pizza, int count) {
        if (isPositivePizzaCount(count) && pizzasCount.containsKey(pizza)) {
            if (!isPositivePizzaCount(pizzasCount.get(pizza) - count)) {
                pizzasCount.remove(pizza);
            } else {
                changePizzaCount(pizza, -count);
            }
            invalidatePriceCache();
        }
    }

    private boolean isPositivePizzaCount(int pizzaCount) {
        return pizzaCount > 0;
    }

    private void changePizzaCount(Pizza pizza, int delta) {
        pizzasCount.put(pizza, pizzasCount.get(pizza) + delta);
    }

    private void invalidatePriceCache() {
        orderPrice = -1;
    }

    private double calculateDiscountPrice() {
        int sum = 0;
        for (AbstractDiscount discount : discounts) {
            sum += discount.calculateDiscount(this);
        }
        return sum;
    }

    private double calculatePizzaPrice(Pizza pizza, int count) {
        return pizza.getPrice() * count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Pizza, Integer> getPizzas() {
        return pizzasCount;
    }

    public void setPizzas(Map<Pizza, Integer> pizzas) {
        this.pizzasCount = pizzas;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (pizzasCount != null ? !pizzasCount.equals(order.pizzasCount) : order.pizzasCount != null) return false;
        if (!customer.equals(order.customer)) return false;
        if (discounts != null ? !discounts.equals(order.discounts) : order.discounts != null) return false;
        return currentState == order.currentState;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + customer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", pizzasCount=" + pizzasCount +
                ", customer=" + customer +
                ", discounts=" + discounts +
                ", orderPrice=" + orderPrice +
                ", currentState=" + currentState +
                '}';
    }
}
