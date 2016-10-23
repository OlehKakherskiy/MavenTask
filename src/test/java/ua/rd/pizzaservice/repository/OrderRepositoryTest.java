package ua.rd.pizzaservice.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleh_Kakherskyi on 10/10/2016.
 */
public class OrderRepositoryTest {

    private static ConfigurableApplicationContext applicationContext;

    private OrderRepository orderRepository;

    @BeforeClass
    public static void beforeClass() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"appContext.xml"});
    }

    @Before
    public void setUp() throws Exception {
        orderRepository = new InMemOrderRepository();
    }

    @Test
    public void saveOrderFirstOne() throws Exception {
        Customer customer = new Customer(1L, "name", "address", null);
        Map<Pizza, Integer> pizzas = new HashMap<>();
        pizzas.put(new Pizza(1, "n", 1.0, PizzaType.MEAT), 1);
        Order order = new Order(customer, pizzas, new ArrayList<>());
        orderRepository.saveOrder(order);
        Assert.assertEquals(1L, order.getId().longValue());
    }

}