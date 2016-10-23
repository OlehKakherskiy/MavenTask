package ua.rd.pizzaservice.services;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.CustomerCard;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by Oleh_Kakherskyi on 10/10/2016.
 */
public class OrderServiceTest {

    private Customer customer = new Customer(1L, "name", "address", null);

    @Test
    public void testPlaceNewOrderAndCheckAddPriceToCustomerNullCard() throws Exception {
        OrderRepository orderRepository = mock(OrderRepository.class);

        Map<Pizza, Integer> pizzas = new HashMap<Pizza, Integer>() {{
            put(new Pizza(1, "name1", 2.0, PizzaType.MEAT), 1);
            put(new Pizza(2, "name2", 3.0, PizzaType.SEA), 2);
        }};

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Order order1 = invocation.getArgumentAt(0, Order.class);
                order1.setId(1L);
                return null;
            }
        }).when(orderRepository).saveOrder(new Order(customer, pizzas, new ArrayList<>()));

        PizzaService pizzaService = getPizzaServiceMock(new Pizza(1, "name1", 2.0, PizzaType.MEAT), new Pizza(2, "name2", 3.0, PizzaType.SEA));

        OrderService orderService = new SimpleOrderService(orderRepository, pizzaService, 10, new ArrayList<>());

        Order order = new Order(customer, pizzas, new ArrayList<>());
        order.setId(1L);
        Map<Integer, Integer> pizzaIdAndCount = new HashMap<Integer, Integer>() {{
            put(1, 1);
            put(2, 2);
        }};
        Assert.assertEquals(order, orderService.placeNewOrder(customer, pizzaIdAndCount));
        Assert.assertNull(customer.getCustomerCard());
    }

    @Test
    public void testPlaceNewOrderAndCheckAddPriceToCustomerCard() throws Exception {
        OrderRepository orderRepository = mock(OrderRepository.class);
        Map<Pizza, Integer> pizzas = new HashMap<Pizza, Integer>() {{
            put(new Pizza(1, "name1", 2.0, PizzaType.MEAT), 1);
            put(new Pizza(2, "name2", 3.0, PizzaType.SEA), 2);
        }};
        Customer customer = new Customer(1L, "name", "address", new CustomerCard(1L, 40.0));
        Customer expectedCustomer = new Customer(1L, "name", "address", new CustomerCard(1L, 48.0));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Order order1 = invocation.getArgumentAt(0, Order.class);
                order1.setId(1L);
                return null;
            }
        }).when(orderRepository).saveOrder(new Order(customer, pizzas, new ArrayList<>()));

        PizzaService pizzaService = getPizzaServiceMock(new Pizza(1, "name1", 2.0, PizzaType.MEAT), new Pizza(2, "name2", 3.0, PizzaType.SEA));

        OrderService orderService = new SimpleOrderService(orderRepository, pizzaService, 10, new ArrayList<>());



        Order order = new Order(expectedCustomer, pizzas, new ArrayList<>());
        order.setId(1L);
        Map<Integer, Integer> pizzaIdAndCount = new HashMap<Integer, Integer>() {{
            put(1, 1);
            put(2, 2);
        }};

        Assert.assertEquals(order, orderService.placeNewOrder(customer, pizzaIdAndCount));
    }

    @Test(expected = IllegalArgumentException.class)
    public void placeNewOrderPizzasCountOverflow() throws Exception {
        OrderService orderService = new SimpleOrderService(mock(OrderRepository.class), mock(PizzaService.class), 10, new ArrayList<>());
        Map<Integer, Integer> pizzaCountMap = new HashMap<Integer, Integer>() {{
            put(1, 5);
            put(2, 4);
            put(3, 3);
        }};
        orderService.placeNewOrder(customer, pizzaCountMap);
    }


    @Test
    public void getOrderPriceById() throws Exception {
        OrderRepository orderRepository = mock(OrderRepository.class);
        Map<Pizza, Integer> pizzas = new HashMap<Pizza, Integer>() {{
            put(new Pizza(1, "name1", 2.0, PizzaType.MEAT), 1);
            put(new Pizza(2, "name2", 3.0, PizzaType.SEA), 2);
        }};

        when(orderRepository.readOrder(1L)).thenReturn(new Order(null, pizzas, Collections.emptyList()));

        OrderService orderService = new SimpleOrderService(orderRepository, null, 10, Collections.emptyList());
        Assert.assertEquals(8.0, orderService.getOrderPrice(1L), 0.000000000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOrderPriceByIdNoOrder() throws Exception {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.readOrder(1L)).thenThrow(IllegalArgumentException.class);

        OrderService orderService = new SimpleOrderService(orderRepository, null, 10, Collections.emptyList());
        Assert.assertEquals(8.0, orderService.getOrderPrice(1L), 0.000000000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOrderPriceFromNullObject() throws Exception {
        OrderService orderService = new SimpleOrderService(mock(OrderRepository.class), mock(PizzaService.class), 10, Collections.emptyList());
        orderService.getOrderPrice(null);
    }

    private PizzaService getPizzaServiceMock(Pizza... pizzas) {
        PizzaService pizzaService = mock(PizzaService.class);
        for (Pizza pizza : pizzas) {
            when(pizzaService.find(pizza.getId())).thenReturn(pizza);
        }
        return pizzaService;
    }
}