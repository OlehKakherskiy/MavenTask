package ua.rd.pizzaservice.domain;

import org.junit.Assert;
import org.junit.Test;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;

import java.util.*;

import static org.mockito.Mockito.mock;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class OrderTest {

    private static final double DELTA = 0.00000000001;

    @Test
    public void calculateOrderPrice() throws Exception {
        List<AbstractDiscount> orderDiscounts = new ArrayList<>();
        orderDiscounts.add(new MaxPizzaPriceDiscount(3, 0.5));
        orderDiscounts.add(new AbstractDiscount() {
            @Override
            public double calculateDiscount(Order order) {
                return 100;
            }

            @Override
            protected boolean canBeApplied(Order order) {
                return true;
            }
        });
        Assert.assertEquals(725.0,
                createOrder(getPizzas(), orderDiscounts).calculateTotalPrice(), DELTA);
    }

    @Test
    public void calculateOrderPrice_noDiscounts() throws Exception {
        Assert.assertEquals(900.0, createOrder(getPizzas(), Collections.emptyList()).calculateTotalPrice(), DELTA);
    }

    private Map<Pizza, Integer> getPizzas() {
        Map<Pizza, Integer> pizzas = new HashMap<>();
        pizzas.put(new Pizza(1, "name", 100.0, PizzaType.SEA), 3);
        pizzas.put(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 4);
        return pizzas;
    }

    @Test
    public void testAddDiscount() throws Exception {
        List<AbstractDiscount> discountList = new ArrayList<>();
        Order order = new Order(null, Collections.emptyMap(), discountList);
        order.addDiscount(new AbstractDiscount() {
            @Override
            public double calculateDiscount(Order order) {
                return 0;
            }

            @Override
            protected boolean canBeApplied(Order order) {
                return false;
            }
        });
        Assert.assertEquals(1, discountList.size());
    }

    @Test
    public void addDiscountNullOne() throws Exception {
        List<AbstractDiscount> discountList = new ArrayList<>();
        Order order = new Order(null, new HashMap<>(), discountList);
        order.addDiscount(null);
        Assert.assertEquals(0, discountList.size());
    }

    @Test
    public void testGetPizzaCount() throws Exception {
        Order order = new Order(null, getPizzaMap(2, 3), new ArrayList<>());
        Assert.assertEquals(5, order.getPizzaCount());
    }

    @Test
    public void testGetPizzaCountFromEmptyOrder() throws Exception {
        Assert.assertEquals(0, createEmptyOrder().getPizzaCount());
    }

    @Test
    public void testAddPizza_addNewOne() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        Map<Pizza, Integer> expectation = getPizzas();
        expectation.put(new Pizza(3, "name3", 200.0, PizzaType.VEGETARIAN), 5);
        order.addPizza(new Pizza(3, "name3", 200.0, PizzaType.VEGETARIAN), 5);
        Assert.assertEquals(expectation, order.getPizzas());
    }

    @Test
    public void testAddPizza_addToExistedPizza() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        Map<Pizza, Integer> expectation = getPizzas();
        expectation.put(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 8);
        order.addPizza(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 4);
        Assert.assertEquals(expectation, order.getPizzas());
    }

    @Test
    public void testAddPizza_CountIsNegative() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        order.addPizza(mock(Pizza.class), -1);
        Assert.assertEquals(getPizzas(), order.getPizzas());
    }

    @Test
    public void testAddPizza_zeroPizzas() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        order.addPizza(mock(Pizza.class), 0);
        Assert.assertEquals(getPizzas(), order.getPizzas());
    }

    @Test
    public void testChangeOrderState() throws Exception {
        Order order = createOrder(Collections.emptyMap(), Collections.emptyList());
        order.changeState(OrderState.CANCELLED);
        Assert.assertEquals(OrderState.CANCELLED, order.getCurrentState());
    }

    @Test
    public void removePizza_removeEnoughPizzaFromExistedOne() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        Map<Pizza, Integer> expectation = getPizzas();
        expectation.put(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 3);
        order.removePizza(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 1);
        Assert.assertEquals(expectation, order.getPizzas());
    }

    @Test
    public void removePizza_removeMorePizzaThanExists() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        Map<Pizza, Integer> expectation = new HashMap<Pizza, Integer>() {{
            put(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 4);
        }};
        order.removePizza(new Pizza(1, "name", 100.0, PizzaType.SEA), 4);
        Assert.assertEquals(expectation, order.getPizzas());
    }

    @Test
    public void removePizza_removeNotExistedPizza() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        order.removePizza(mock(Pizza.class), 1);
        Assert.assertEquals(getPizzas(), order.getPizzas());
    }

    @Test
    public void removePizza_negativeCount() throws Exception {
        Order order = createOrder(getPizzas(), Collections.emptyList());
        order.removePizza(new Pizza(2, "name2", 150.0, PizzaType.MEAT), -2);
        Assert.assertEquals(getPizzas(), order.getPizzas());
    }

    @Test
    public void removePizza_zeroCount() throws Exception{
        Order order = createOrder(getPizzas(), Collections.emptyList());
        order.removePizza(new Pizza(2, "name2", 150.0, PizzaType.MEAT), 0);
        Assert.assertEquals(getPizzas(), order.getPizzas());
    }


    private Order createEmptyOrder() {
        return createOrder(Collections.emptyMap(), Collections.emptyList());
    }

    private Order createOrder(Map<Pizza, Integer> pizzas, List<AbstractDiscount> discounts) {
        return new Order(null, pizzas, discounts);
    }

    private Map<Pizza, Integer> getPizzaMap(int... pizzasCount) {
        Map<Pizza, Integer> result = new HashMap<>();
        for (int i : pizzasCount) {
            result.put(mock(Pizza.class), i);
        }
        return result;
    }
}