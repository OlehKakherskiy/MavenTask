package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.repository.PizzaRepository;
import ua.rd.pizzaservice.services.OrderService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrii
 */
public class SpringAppRunner {
    public static void main(String[] args) {
        System.out.println("Hello world with args "+ Arrays.toString(args));
        ConfigurableApplicationContext appContext =
                new ClassPathXmlApplicationContext(new String[]{"appContext.xml"});
        System.out.println(Arrays.toString(appContext.getBeanDefinitionNames()));


        PizzaRepository pizzaRepository = (PizzaRepository) appContext.getBean("pizzaRepository");
        System.out.println(pizzaRepository.find(1));

        OrderService orderService = (OrderService) appContext.getBean("orderService");
        Map<Integer, Integer> pizzasAndCounts = new HashMap<>();
        pizzasAndCounts.put(1, 2);
        pizzasAndCounts.put(2, 3);
        pizzasAndCounts.put(3, 1);
        Order order = orderService.placeNewOrder(new Customer(1L, "ol", "ad", null), pizzasAndCounts);
        System.out.println(order);
        order.changeState(OrderState.DONE);
        System.out.println(order);
        orderService.addPizza(order.getId(), 1, 2);
        System.out.println(order);
        appContext.close();
    }

}
